package com.example.testspring.util;

import com.example.testspring.entities.*;
import com.example.testspring.services.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeneratorPdfGradeReport {

    private final UsersService usersService;
    private final StudentService studentService;
    private final ClassesService classesService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final SubjectService subjectService;

    @Autowired
    public GeneratorPdfGradeReport(UsersService usersService, StudentService studentService, ClassesService classesService, CourseService courseService, GradeService gradeService, SubjectService subjectService){
        this.usersService = usersService;
        this.studentService = studentService;
        this.classesService = classesService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.subjectService = subjectService;
    }

    // generate a grade report
    public byte[] generatePDF(Users user) {
        Document document = new Document();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // title and student information
            addTitleAndStudentInfo(document, user);

            // fetch and organize grades by subject and course
            Map<String, Map<String, List<Grade>>> subjectCourseGrades = fetchSubjectCourseGrades(user.getUserId());

            // report section for each subject
            generateSubjectSections(document, subjectCourseGrades);

            // overall mean at the end of the report
            addOverallAverageSection(document, subjectCourseGrades);

            document.close();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    // Add the title and the student information
    private void addTitleAndStudentInfo(Document document, Users user) throws DocumentException {
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Relevé de notes", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Student info
        Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);
        document.add(new Paragraph("Étudiant: " + user.getUserName() + " " + user.getUserLastName(), infoFont));
        document.add(new Paragraph("Mail: " + user.getUserEmail(), infoFont));
        String class_name = "Aucune";

        Integer classId = studentService.getStudentById(user.getUserId()).getClassId();
        if(classId != null) {
            class_name = classesService.getClasseById(classId).getClassName();
        }

        document.add(new Paragraph("Classe: " + class_name, infoFont));
        document.add(Chunk.NEWLINE);
    }

    // fetch and organize grades by subject and course.
    // in public because we need it for the Servlet
    public Map<String, Map<String, List<Grade>>> fetchSubjectCourseGrades(int userId) {
        Map<String, Map<String, List<Grade>>> subjectCourseGrades = new LinkedHashMap<>();

        List<Subjects> subjects = subjectService.getSubjectsByStudentId(userId);

        if (subjects != null) {
            for (Subjects subject : subjects) {
                Map<String, List<Grade>> courseGradesMap = new LinkedHashMap<>();
                List<Course> courses = courseService.getCoursesBySubjectId(subject.getSubjectId());

                for (Course course : courses) {

                    List<Grade> grades = gradeService.getGradeByStudentId(userId).stream()
                            .filter(grade -> grade.getCourseId() == course.getCourseId())
                            .collect(Collectors.toList());
                    courseGradesMap.put(course.getCourseName(), grades);

                }

                subjectCourseGrades.put(subject.getSubjectName(), courseGradesMap);

            }
        }

        return subjectCourseGrades;
    }

    // generate section of the PDF for every subject
    private void generateSubjectSections(Document document, Map<String, Map<String, List<Grade>>> subjectCourseGrades) throws DocumentException {
        for (Map.Entry<String, Map<String, List<Grade>>> subjectEntry : subjectCourseGrades.entrySet()) {
            String subjectName = subjectEntry.getKey();
            Map<String, List<Grade>> courseGradesMap = subjectEntry.getValue();

            // add subject name
            Font subjectFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph(subjectName, subjectFont));
            document.add(Chunk.NEWLINE);

            // add each course and its grades
            for (Map.Entry<String, List<Grade>> courseEntry : courseGradesMap.entrySet()) {

                String courseName = courseEntry.getKey();
                List<Grade> grades = courseEntry.getValue();
                addCourseDetailsAndGradesTable(document, courseName, grades);
            }
        }
    }

    // add detail of the course and the grades in a table
    private void addCourseDetailsAndGradesTable(Document document, String courseName, List<Grade> grades) throws DocumentException {
        // course name
        Font courseFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Paragraph courseParagraph = new Paragraph(courseName, courseFont);

        // space between course name and table
        courseParagraph.setSpacingAfter(5f);
        document.add(courseParagraph);

        // table grade
        PdfPTable gradesTable = createGradesTable(grades);
        document.add(gradesTable);

        // mean for the course
        double courseAverage = calculateAverage(grades);
        Font averageFont = new Font(Font.FontFamily.HELVETICA, 12);
        Paragraph courseAverageParagraph = new Paragraph("Moyenne du cours: " + String.format("%.2f", courseAverage), averageFont);
        document.add(courseAverageParagraph); // Add the course average after the table

        document.add(Chunk.NEWLINE);
    }


    // create the table for the grades
    private PdfPTable createGradesTable(List<Grade> grades) {
        PdfPTable table = new PdfPTable(3); // Columns: Intitulé, Note, Coeff
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Table headers
        table.addCell("Intitulé");
        table.addCell("Note");
        table.addCell("Coeff");

        // Add grade details
        for (Grade grade : grades) {
            table.addCell(grade.getGradeName());
            table.addCell(String.valueOf(grade.getGradeValue()));
            table.addCell(String.valueOf(grade.getGradeCoefficient()));
        }

        return table;
    }

    // calculate mean/average
    // in public too beacuse usefull for the Servlet
    public double calculateAverage(List<Grade> grades) {
        double weightedSum = 0;
        int totalCoeff = 0;

        for (Grade grade : grades) {
            weightedSum += grade.getGradeValue() * grade.getGradeCoefficient();
            totalCoeff += grade.getGradeCoefficient();
        }

        return totalCoeff > 0 ? (weightedSum / totalCoeff) : 0;
    }

    // add the overall average to the doc
    private void addOverallAverageSection(Document document, Map<String, Map<String, List<Grade>>> subjectCourseGrades) throws DocumentException {
        List<Grade> allGrades = subjectCourseGrades.values().stream()
                .flatMap(courses -> courses.values().stream())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        double overallAverage = calculateAverage(allGrades);

        // Add a section for the overall average
        Font overallAverageFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        document.add(new Paragraph("Moyenne Générale: " + String.format("%.2f", overallAverage), overallAverageFont));
    }
}
