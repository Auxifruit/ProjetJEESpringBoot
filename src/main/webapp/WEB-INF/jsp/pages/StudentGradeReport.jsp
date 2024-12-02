<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page import="com.example.testspring.entities.Grade" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Relevé de notes</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
  <h1>Relevé de notes</h1>
  <div id="OldInfos">

    <!-- Student information -->
    <div class="student-info">
      <p><strong>Étudiant :</strong>
        <%= ((Users) request.getAttribute("student")) != null ? ((Users) request.getAttribute("student")).getUserName() + " " + ((Users) request.getAttribute("student")).getUserLastName() : "Non disponible" %>
      </p>
      <p><strong>Mail :</strong>
        <%= ((Users) request.getAttribute("student")) != null ? ((Users) request.getAttribute("student")).getUserEmail() : "Non disponible" %>
      </p>
      <p><strong>Classe :</strong> <%= request.getAttribute("className") != null ? request.getAttribute("className") : "Non disponible" %></p>
    </div>

    <!-- show the subjects, the courses and the grades of the student -->
    <%
      // we use the same pattern for the PDF Generator
      Map<String, Map<String, List<Grade>>> subjectCourseGrades = (Map<String, Map<String, List<Grade>>>) request.getAttribute("subjectCourseGrades");

      if (subjectCourseGrades != null && !subjectCourseGrades.isEmpty()) {
        // for every subject ..
        for (Map.Entry<String, Map<String, List<Grade>>> subjectEntry : subjectCourseGrades.entrySet()) {
          String subjectName = subjectEntry.getKey();
    %>
    <div class="subject">
      <h3><%= subjectName %></h3>
      <%
        // for every course ..
        Map<String, List<Grade>> courses = subjectEntry.getValue();
        for (Map.Entry<String, List<Grade>> courseEntry : courses.entrySet()) {
          String courseName = courseEntry.getKey();
          List<Grade> grades = courseEntry.getValue();

          // calculate the average for the course
          double courseAverage = 0.0;
          int totalCoeff = 0;
          if (grades != null && !grades.isEmpty()) {
            for (Grade grade : grades) {
              courseAverage += grade.getGradeValue() * grade.getGradeCoefficient();
              totalCoeff += grade.getGradeCoefficient();
            }
            courseAverage = (totalCoeff > 0) ? (courseAverage / totalCoeff) : 0.0;
          }
      %>
      <h4><%= courseName %></h4>
      <table>
        <thead>
        <tr>
          <th>Intitulé</th>
          <th>Note</th>
          <th>Coefficient</th>
        </tr>
        </thead>
        <tbody>
        <%
          if (grades != null && !grades.isEmpty()) {
            // .. enumerate every grade
            for (Grade grade : grades) {
        %>
        <tr>
          <td><%= grade.getGradeName() %></td>
          <td><%= grade.getGradeValue() %></td>
          <td><%= grade.getGradeCoefficient() %></td>
        </tr>
        <%
          }
        } else {
        %>
        <tr>
          <td colspan="3" style="text-align: center;">Aucune note disponible</td>
        </tr>
        <%
          }
        %>
        </tbody>
      </table>
      <!-- mean/average of the course -->
      <p><strong>Moyenne du cours :</strong> <%= String.format("%.2f", courseAverage) %></p>
      <%
        }
      %>
    </div>
    <%
      }
    } else {
    %>
    <p>Aucune matière disponible.</p>
    <%
      }
    %>

    <!-- Mean/Average -->
    <div class="student-info">
      <p><strong>Moyenne Générale :</strong> <%= request.getAttribute("mean") %></p>
    </div>
  </div>
  <!-- button pdf file -->
  <div class="download-button">
    <form action="/projetSB/StudentGradeReportController" method="post">
      <input type="hidden" name="userId" value="<%= ((Users) request.getAttribute("student")).getUserId() %>">
      <button type="submit">Télécharger au format PDF</button>
    </form>
  </div>
</div>
</body>
</html>