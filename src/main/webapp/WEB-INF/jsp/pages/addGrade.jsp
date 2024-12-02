<%--
  Created by IntelliJ IDEA.
  User: Amaury
  Date: 15/11/2024
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page import="com.example.testspring.entities.Course" %>
<%@ page import="com.example.testspring.entities.Classes" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Gestion des Notes</title>
  <link rel="stylesheet" type="text/css" href="${PageContext.request.contextPath}/Styles/SaisieNote.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      display: flex;
      justify-content: center;
    }

    .container {
      width: 80%;
      margin: 20px;
      padding: 20px;
      border: 1px solid #ccc;
      border-radius: 8px;
      background-color: #f5f5f5;
    }

    .subject-buttons, .class-buttons {
      display: flex;
      gap: 10px;
      margin-bottom: 20px;
    }

    .subject-button, .class-button {
      padding: 10px;
      border-radius: 6px;
      color: white;
      border: none;
      cursor: pointer;
      background-color: #a0c4ff; /* Couleur bleue */
    }

    .subject-button.selected {
      background-color: #4a90e2; /* Couleur plus sombre lorsque sélectionné */
    }

    .class-button.selected {
      background-color: #4a90e2; /* Couleur plus sombre lorsque sélectionné */
    }

    /* Section formulaire pour ajouter une note */
    .form-section {
      display: flex;
      flex-direction: column;
      gap: 10px;
      margin-bottom: 20px;
    }

    .input-field, .text-area {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .submit-button {
      padding: 10px;
      background-color: #32cd32;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
    }

    .notes-table, .note-entry, .student-note, .student-appreciation {
      margin-top: 20px;
    }
  </style>
  <script>
    function selectDiscipline(courseId) {
      // Réinitialiser l'état des boutons
      const buttons = document.querySelectorAll('.subject-button');
      buttons.forEach(button => button.classList.remove('selected'));

      // Ajouter l'état sélectionné
      const selectedButton = document.getElementById('discipline-' + courseId);
      if (selectedButton) {
        selectedButton.classList.add('selected');
      }

      // Mettre à jour le champ caché avec l'ID de la discipline
      document.getElementById('selected-course-id').value = courseId;
    }

    function selectClass(classId) {
      // Réinitialiser l'état des boutons
      const buttons = document.querySelectorAll('.class-button');
      buttons.forEach(button => button.classList.remove('selected'));

      // Ajouter l'état sélectionné
      const selectedButton = document.getElementById('class-' + classId);
      if (selectedButton) {
        selectedButton.classList.add('selected');
      }

      // Mettre à jour le champ caché avec l'ID de la classe
      document.getElementById('selected-class-id').value = classId;
    }

    function validateCriteria() {
      const courseId = document.getElementById('selected-course-id').value;
      const classId = document.getElementById('selected-class-id').value;

      if (!courseId || !classId) {
        alert("Veuillez sélectionner un cours et une classe avant de continuer.");
        return false; // Empêche la soumission
      }

      document.getElementById('criteria-form').submit();
    }
  </script>
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div class="container">

  <!-- Teacher -->
  <%
    Users connectedUser = (Users) session.getAttribute("connectedUser");
    if(connectedUser == null || !Role.teacher.equals(connectedUser.getUserRole())) {
      response.sendRedirect("index");
      return;
    }
  %>
  <h2>Professeur: <%= connectedUser.getUserName() %> <%= connectedUser.getUserLastName() %></h2>

  <!-- Discipline -->
  <div class="subject-buttons">
    <%
      List<Course> disciplines = (List<Course>) request.getAttribute("disciplines");
      if (disciplines != null && !disciplines.isEmpty()) {
        for (Course discipline : disciplines) {
    %>
    <button type="button"
            class="subject-button"
            id="discipline-<%= discipline.getCourseId() %>"
            onclick="selectDiscipline('<%= discipline.getCourseId() %>')">
      <%= discipline.getCourseName() %>
    </button>
    <%
      }
    } else {
    %>
    <p>Pas de matière disponible.</p>
    <%
      }
    %>
  </div>

  <!-- Classes -->
  <div class="class-buttons">
    <%
      List<Classes> classes = (List<Classes>) request.getAttribute("classes");
      if (classes != null && !classes.isEmpty()) {
        for (Classes classe : classes) {
    %>
    <button type="button"
            class="class-button"
            id="class-<%= classe.getClassId() %>"
            onclick="selectClass('<%= classe.getClassId() %>')">
      <%= classe.getClassName() %>
    </button>
    <%
      }
    } else {
    %>
    <p>Pas de classe disponible.</p>
    <%
      }
    %>
  </div>

  <!-- Formulaire POST pour envoyer les critères -->
  <form action="/projetSB/EntryNoteController" method="POST" id="criteria-form">
    <input type="hidden" name="courseId" id="selected-course-id" value="">
    <input type="hidden" name="classId" id="selected-class-id" value="">
    <input type="hidden" name="teacherID" value="<%=request.getAttribute("teacherID")%>">
    <button type="submit" id="submit-button" onclick="validateCriteria()">OK</button>
  </form>

  <hr>

  <!-- Table to display students -->
  <form action="/projetSB/AddGradeController" method="post">
    <div class="form-section">
      <label>Intitulé du contrôle</label>
      <input type="text" name="gradeName" class="input-field" required>

      <label>Choix Coefficient</label>
      <input type="text" name="gradeCoefficient" class="input-field" required>
    </div>

    <input type="hidden" name="courseId" value="<%=request.getAttribute("courseId")%>">
    <input type="hidden" name="teacherID" value="<%=request.getAttribute("teacherID")%>">

    <div class="notes-table">
      <h3>Liste des étudiants</h3>
      <table style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
        <tr>
          <th>Prénom</th>
          <th>Nom</th>
          <th>Email</th>
          <th>Note</th>
        </tr>
        </thead>
        <tbody>
        <%
          List<Users> students = (List<Users>) request.getAttribute("students");
          if (students != null && !students.isEmpty()) {
            for (Users student : students) {
        %>
        <tr>
          <td><%= student.getUserName() %></td>
          <td><%= student.getUserLastName() %></td>
          <td><%= student.getUserEmail() %></td>
          <td>
            <input type="number" name="grade_<%= student.getUserId() %>"
                   min="0" max="20" step="0.1" placeholder="Note" required>
          </td>
        </tr>
        <%
          }
        } else {
        %>
        <tr>
          <td colspan="4" style="text-align: center;">Aucun étudiant trouvé</td>
        </tr>
        <% } %>
        </tbody>
      </table>
    </div>

    <button type="submit" class="submit-button" style="margin-top: 20px;">Valider toutes les notes</button>
  </form>
</div>
</body>
</html>
