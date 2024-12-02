<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
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
    <title>Modification de note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Modification d'une note</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Grade grade = (Grade) request.getAttribute("grade");
        List<Course> courseList = (List<Course>) request.getAttribute("courseList");
        List<Users> studentList = (List<Users>) request.getAttribute("studentList");
        List<Users> teacherList = (List<Users>) request.getAttribute("teacherList");

        Course gradeCourse = (Course) request.getAttribute("gradeCourse");
        Users gradeTeacher = (Users) request.getAttribute("gradeTeacher");
        Users gradeStudent = (Users) request.getAttribute("gradeStudent");
        Classes studentClass = (Classes) request.getAttribute("studentClass");
        Map<Integer, Classes> mapStudentIdClass = (Map<Integer, Classes>) request.getAttribute("mapStudentIdClass");
        Map<Integer, Users> mapStudentIdUsers = (Map<Integer, Users>) request.getAttribute("mapStudentIdUsers");

        if (grade == null) {
    %>
    <p>La note n'existe pas</p>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Anciennes informations : </h3>
            <p>Nom de la note : <%= grade.getGradeName() %></p>
            <p>Nom du cours :
                <% if(gradeCourse == null) {
                %>
                Pas de cours associé
                <%
                } else {
                %>
                <%= gradeCourse.getCourseName() %>
                <%
                    }
                %>
            </p>
            <p>Valeur de la note : <%= grade.getGradeValue() %></p>
            <p>Coefficient de la note : <%= grade.getGradeCoefficient() %></p>
            <p>Nom et prénom de l'étudiant : <%= gradeStudent.getUserLastName() + " " + gradeStudent.getUserName() %></p>
            <p>Classe de l'étudiant :
                <% if(gradeTeacher == null) {
                %>
                Pas de classe associé
                <%
                } else {
                %>
                <%= studentClass.getClassName() %>
                <%
                    }
                %>
            </p>
            <p>Nom et prénom de l'enseignant :
                <% if(gradeTeacher == null) {
                %>
                Pas d'enseignant associé
                <%
                } else {
                %>
                <%= gradeTeacher.getUserLastName() + " " + gradeTeacher.getUserName() %>
                <%
                    }
                %>
            </p>
            <%
                }
            %>
    </div>
    <form action="/projetSB/GradeModificationController" method="post">
        <h3>Nouvelles informations :</h3>
        <label>Nom de la note : </label>
        <input type="text" name="gradeNewName"/>

        <label>Choix du cours : </label>
        <select name="gradeNewCourseId">
            <option value="">Ne pas changer le cours</option>
            <%
                for(Course course : courseList) {
            %>
            <option value="<%= course.getCourseId() %>"><%= course.getCourseName() %></option>
            <%
                }
            %>
        </select>

        <label>Valeur de la note : </label>
        <input type="number" name="gradeNewValue"/>

        <label>Coefficient de la note : </label>
        <input type="number" name="gradeNewCoefficient"/>

        <label>Choix de l'étudiant : </label>
        <select name="gradeNewStudentId">
            <option value="">Ne pas changer l'étudiant</option>
            <%
                for(Users student : studentList) {
                    Classes sClass = mapStudentIdClass.get(student.getUserId());
                    Users u = mapStudentIdUsers.get(student.getUserId());
                    if(sClass != null) {
            %>
            <option value="<%= student.getUserId() %>"><%= u.getUserLastName() + " " + u.getUserName() + " " + sClass.getClassName() %></option>
            <%
                    }
                }
            %>
        </select>

        <label>Choix de l'enseignant : </label>
        <select name="gradeNewTeacherId">
            <option value="">Ne pas changer l'enseignant</option>
            <%
                for(Users teacher : teacherList) {
            %>
            <option value="<%= teacher.getUserId() %>"><%= teacher.getUserLastName() + " " + teacher.getUserName()%></option>
            <%
                }
            %>
        </select>

        <input type="text" name="gradeId" value="<%= grade.getGradeId() %>" style="display: none">

        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %>
        </p>
        <%
            }
        %>
        <button type="submit" onclick="confirmModify(event)">Modifier</button>
    </form>
</div>
</body>
<script>
    function confirmModify(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir modifier la note ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
