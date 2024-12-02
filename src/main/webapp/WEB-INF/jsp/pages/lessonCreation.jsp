<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 10/11/2024
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="java.util.Map" %>
<br>
<head>
    <title>Création séance</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/showTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
  <h1>Création d'une séance</h1>
  <div id="OldInfos">
    <h3>Séance existante :</h3>
  <%
    Users connectedUser = (Users) session.getAttribute("connectedUser");
    if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
      response.sendRedirect("index");
      return;
    }

    List<Course> coursesList = (List<Course>) request.getAttribute("courses");
    List<Lesson> lessonList = (List<Lesson>) request.getAttribute("lessons");
    Map<Integer, Users> mapLessonIdTeacher = (Map<Integer, Users>) request.getAttribute("mapLessonIdTeacher");
    Map<Integer, Course> mapLessonIdCourse = (Map<Integer, Course>) request.getAttribute("mapLessonIdCourse");
    Map<Integer, Users> mapTeacherIdUsers = (Map<Integer, Users>) request.getAttribute("mapTeacherIdUsers");

  if (lessonList == null || lessonList.isEmpty()) {
  %>
  <h2>Pas de séance pour l'instant</h2>
  <%
  } else {
  %>
    <button onclick="toggleTable()">Afficher/Masquer le tableau</button></br></br>
    <table border="1" id="existingTable" style="display: table">
      <tr>
        <th>Nom du cours</th>
        <th>Nom et prénom du professeur</th>
        <th>Date de début</th>
        <th>Date de fin</th>
      </tr>
      <%
        for (Lesson lesson : lessonList) {

      %>
      <tr>
        <td>
          <%
            Course course = mapLessonIdCourse.get(lesson.getLessonId());
            if(course == null) {
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
        <td>
          <%
            Users teacher = mapLessonIdTeacher.get(lesson.getLessonId());
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
        <td><%= lesson.getLessonStartDate() %></td>
        <td><%= lesson.getLessonEndDate() %></td>
      </tr>
      <%
        }
      %>
    </table>
  <%
    }
  %>
  </div>
  <form action="/projetSB/LessonCreationController" method="post">
    <%
      List<Course> courseList = (List<Course>) request.getAttribute("courses");

      if (courseList == null || courseList.isEmpty()) {
    %>
    <h3>Pas de cours pour faire la séance</h3>
    <%
    } else {
    %>
        <label>Choix du cours : </label>
        <select name="course">
          <%
            for (Course course : coursesList) {
          %>
          <option value=<%= course.getCourseId() %>><%= course.getCourseName()%></option>
          <%
            }
          %>
        </select>
    <%
      }
    %>

  <label>Date de début : </label>
  <input name="startDate" type="datetime-local" required/>

  <label>Date de fin : </label>
  <input name="endDate" type="datetime-local" required/>

  <%
    List<Teacher> teacherList = (List<Teacher>) request.getAttribute("teachers");

    if (teacherList == null || teacherList.isEmpty()) {
  %>
  <h3>Pas d'enseignant pour faire la séance</h3>
  <%
  } else {
  %>
  <label>Choix du professeur : </label>
  <select name="teacher">
    <%
      for (Teacher teacher : teacherList) {
        Users u = mapTeacherIdUsers.get(teacher.getTeacherId());

        String teacherLastName = u.getUserLastName();
        String teacherName = u.getUserName();

        if(teacherLastName != null || teacherName != null) {
    %>
    <option value=<%= teacher.getTeacherId() %>><%= teacherLastName + " " + teacherName %></option>
    <%
        }
      }
    %>
  </select>
    <%
        }
    %>
    <% String messageErreur = (String) request.getAttribute("erreur");
      if(messageErreur != null && !messageErreur.isEmpty()) {
    %>
    <p style='color: red'><%= messageErreur %></p>
    <%
      }
    %>
  <button type="submit" onclick="confirmCreate(event)">Valider</button>
  </form>
</div>
</body>
<script>
  function confirmCreate(event) {
    const confirmation = confirm("Êtes-vous sûr de vouloir créer la séance ?");

    if (!confirmation) {
      event.preventDefault();
    }
  }
</script>
</html>
