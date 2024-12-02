<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.apache.catalina.User" %>
<%--
Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 12/11/2024
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Création de note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/showTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Création d'une nouvelle note</h1>
    <div id="OldInfos">
        <h3>Notes existante : </h3>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Grade> gradeList = (List<Grade>) request.getAttribute("grades");

        List<Course> courseList = (List<Course>) request.getAttribute("courses");
        List<Student> studentList = (List<Student>) request.getAttribute("students");
        List<Teacher> teacherList = (List<Teacher>) request.getAttribute("teachers");

        Map<Integer, Course> mapGradeIdCourse = (Map<Integer, Course>) request.getAttribute("mapGradeIdCourse");
        Map<Integer, Users> mapGradeIdStudent = (Map<Integer, Users>) request.getAttribute("mapGradeIdStudent");
        Map<Integer, Users> mapStudentIdUsers = (Map<Integer, Users>) request.getAttribute("mapStudentIdUsers");
        Map<Integer, Classes> mapStudentIdClass = (Map<Integer, Classes>) request.getAttribute("mapStudentIdClass");
        Map<Integer, Users> mapGradeIdTeacher = (Map<Integer, Users>) request.getAttribute("mapGradeIdTeacher");
        Map<Integer, Users> mapTeacherIdUsers = (Map<Integer, Users>) request.getAttribute("mapTeacherIdUsers");

        if (gradeList == null || gradeList.isEmpty()) {
    %>
    <p>Il n'y a pas encore de note.</p>
    <%
    } else {
    %>
        <button onclick="toggleTable()">Afficher/Masquer le tableau</button></br></br>
        <table border="1" id="existingTable" style="display: table">
            <th>Nom de la note</th>
            <th>Nom du cours</th>
            <th>Valeur de la note</th>
            <th>Coefficient de la note</th>
            <th>Nom et prénom de l'étudiant</th>
            <th>Nom et prénom de l'enseignant</th>
            <%
                for (Grade grade : gradeList) {
                    Integer courseId = grade.getCourseId();
                    Integer teacherId = grade.getTeacherId();
            %>
            <tr>
                <td><%= grade.getGradeName() %></td>
                <td>
                    <%
                        Course course = mapGradeIdCourse.get(grade.getGradeId());
                        if(courseId == null) {
                    %>
                    Pas de cours associé
                    <%
                    } else {
                    %>
                    <%= course.getCourseName() %>
                    <%
                        }
                    %>
                </td>
                <td><%= grade.getGradeValue() %></td>
                <td><%= grade.getGradeCoefficient() %></td>
                <td>
                    <%
                        Users student = mapGradeIdStudent.get(grade.getGradeId());
                    %>
                    <%= student.getUserLastName() + " " + student.getUserName() %></td>
                <td>
                    <%
                        Users teacher = mapGradeIdTeacher.get(grade.getGradeId());
                        if(teacher == null) {
                    %>
                    Pas d'enseignant associé
                    <%
                    } else {
                    %>
                    <%= teacher.getUserLastName() + " " + teacher.getUserName() %>
                    <%
                        }
                    %>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <%
            }
        %>
    </div>
    <form action="/projetSB/GradeCreationController" method="post">
        <h3>Nouvelle note :</h3>
        <label>Nom de la note : </label>
        <input type="text" name="newGradeName" required/>

        <%
            if(courseList == null || courseList.isEmpty()) {
        %>
        <h3>Pas de cours disponible</h3>
        <%
        } else {
        %>
        <label>Choix du cours : </label>
        <select name="newGradeCourseId" required>
            <%
                for(Course course : courseList) {
            %>
            <option value="<%= course.getCourseId() %>"><%= course.getCourseName() %></option>
            <%
                }
            %>
        </select>
        <%
            }
        %>

        <label>Valeur de la note : </label>
        <input type="number" name="newGradeValue" required/>

        <label>Coefficient de la note : </label>
        <input type="number" name="newGradeCoefficient" required/>

        <%
            if(studentList == null || studentList.isEmpty()) {
        %>
        <h3>Pas d'étudiant disponible</h3>
        <%
            } else {
        %>
        <label>Choix de l'étudiant : </label>
        <select name="newGradeStudentId" required>
            <%
                for(Student student : studentList) {
                    Classes studentClass = mapStudentIdClass.get(student.getClassId());
                    String studentClassString = "";
                    if(studentClass != null) {
                        studentClassString = studentClass.getClassName();
                    }
                    Users u = mapStudentIdUsers.get(student.getStudentId());
                    String userLastName = "Pas de nom";
                    String userName = "Pas de prénom";
                    if(u.getUserLastName() != null) {
                        userLastName = u.getUserLastName();
                    }
                    if(u.getUserName() != null) {
                        userName = u.getUserName();
                    }
            %>
            <option value="<%= student.getStudentId() %>"><%= userLastName + " " + userName + " " + studentClassString %></option>
            <%
                }
            %>
        </select>
        <%
            }
        %>

        <%
            if(teacherList == null || teacherList.isEmpty()) {
        %>
        <h3>Pas d'enseignant disponible</h3>
        <%
            } else {
        %>
        <label>Choix de l'enseignant : </label>
        <select name="newGradeTeacherId" required>
            <%
                for(Teacher teacher : teacherList) {
                    Users u = mapTeacherIdUsers.get(teacher.getTeacherId());
                    String userLastName = "Pas de nom";
                    String userName = "Pas de prénom";
                    if(u.getUserLastName() != null) {
                        userLastName = u.getUserLastName();
                    }
                    if(u.getUserName() != null) {
                        userName = u.getUserName();
                    }
            %>
            <option value="<%= teacher.getTeacherId() %>"><%= userLastName + " " + userName%></option>
            <%
                }
            %>
        </select>
        <%
            }
        %>

        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
            }
        %>
        <button type="submit" onclick="confirmCreate(event)">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmCreate(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir créer la note ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
