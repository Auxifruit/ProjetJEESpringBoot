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
    <title>Modification séance</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
  <h1>Modification d'une séance</h1>
  <%
    Users connectedUser = (Users) session.getAttribute("connectedUser");
    if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
      response.sendRedirect("index");
      return;
    }

    Lesson lesson = (Lesson) request.getAttribute("lesson");
    List<Course> courseList = (List<Course>) request.getAttribute("courses");
    List<Users> teacherList = (List<Users>) request.getAttribute("teacherList");
    Users lessonTeacher = (Users) request.getAttribute("lessonTeacher");
    Course lessonCourse = (Course) request.getAttribute("lessonCourse");

    if (lesson == null) {
  %>
  <h2>Pas de séance à modifier</h2>
  <%
  } else {
  %>
  <div id="OldInfos">
    <h3>Anciennes informations :</h3>
    <p>Ancien cours :
    <%
      if(lessonCourse == null) {
    %>
    Il n'y a pas de Course associé à la séance</p>
    <%
      } else {
    %>
    <%= lessonCourse.getCourseName() %></p>
    <%
      }
    %>
    <p>Ancien enseignant :
    <%
      if(lessonTeacher == null) {
    %>
    Il n'y a pas d'Teacher associé à la séance</p>
    <%
    } else {
    %>
    <%= " " + lessonTeacher.getUserLastName() + " " + lessonTeacher.getUserName() %></p>
    <%
      }
    %>
    <p>Ancienne date de début : <%= lesson.getLessonStartDate() %></p>
    <p>Ancienne date de fin : <%= lesson.getLessonEndDate() %></p>
  </div>

  <form action="/projetSB/LessonModificationController" method="post">
  <h3>Nouvelles informations :</h3>
  <label>Choix du nouveau cours : </label>
    <%
      if (courseList == null || courseList.isEmpty()) {
    %>
    <p>Pas de cours à selectionner</p>
    <%
      }
      else {
    %>
    <select name="newCourseId">
      <option value="">Ne pas modifier le cours</option>
    <%
        for (Course course : courseList) {
    %>
      <option value=<%= course.getCourseId() %>><%= course.getCourseName()%></option>
    <%
        }
    %>
  </select>
    <%
      }
    %>

  <label>Nouvelle date de début : </label>
  <input name="newStartDate" type="datetime-local"/>

  <label>Nouvelle date de fin : </label>
  <input name="newEndDate" type="datetime-local"/>

  <%
    if (teacherList == null || teacherList.isEmpty()) {
  %>
  <h3>Pas d'enseignant à selectionner</h3>
  <%
  } else {
  %>
  <label>Choix de l'enseignant : </label>
  <select name="newTeacherId">
    <option value="">Ne pas modifier l'enseignant</option>
    <%
      for (Users teacher : teacherList) {
        String teacherLastName = teacher.getUserLastName();
        String teacherName = teacher.getUserName();

        if(teacherLastName != null || teacherName != null) {
    %>
    <option value=<%= teacher.getUserId() %>><%= teacherLastName + " " + teacherName %></option>
    <%
        }
      }
    %>
  </select>
  <input name="lessonId" value="<%= lesson.getLessonId() %>" style="display: none">
    <% String messageErreur = (String) request.getAttribute("erreur");
      if(messageErreur != null && !messageErreur.isEmpty()) {
    %>
    <p style='color: red'><%= messageErreur %></p>
    <%
          }
        }
      }
    %>
  <button type="submit" onclick="confirmModify(event)">Valider</button>
  </form>
</div>
</body>
<script>
  function confirmModify(event) {
    const confirmation = confirm("Êtes-vous sûr de vouloir modifier la séance ?");

    if (!confirmation) {
      event.preventDefault();
    }
  }
</script>
</html>
