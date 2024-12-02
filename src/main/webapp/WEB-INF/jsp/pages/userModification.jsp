<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Classes" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page import="com.example.testspring.entities.Major" %>
<%@ page import="com.example.testspring.entities.Role" %><%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 12/11/2024
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification d'utilisateur</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Modification d'un utilisateur</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Users userToModify = (Users) request.getAttribute("userToModify");
        List<Classes> classesList = (List<Classes>) request.getAttribute("classesList");
        List<Major> majorList = (List<Major>) request.getAttribute("majorList");
        Classes studentClasse = (Classes) request.getAttribute("studentClasse");
        Major studentMajor = (Major) request.getAttribute("studentMajor");
        Role role = userToModify.getUserRole();

        if (userToModify == null) {
    %>
    <p>La matière n'existe pas</p>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Anciennes informations</h3>
        <p>Ancien nom : <%= userToModify.getUserLastName() %></p>
        <p>Ancien prénom : <%= userToModify.getUserName() %></p>
        <p>Ancien email : <%= userToModify.getUserEmail() %></p>
        <p>Ancienne date de naissance : <%= userToModify.getUserBirthdate() %></p>
        <%
            if(role.equals(Role.student)) {
            %>
            <p>Ancienne classe :
            <%
                if(studentClasse != null) {
            %>
             <%= studentClasse.getClassName() %>
            <%
            } else {
            %>
            Sans classe
            <%
                }
            %>
            </p>
            <p>Ancienne filière :
            <%
                if(studentMajor != null) {
            %>
            <%= studentMajor.getMajorName() %>
            <%
            } else {
            %>
            Sans filière
            <%
                }
            %>
            </p>
            <p>Ancien rôle : <%= userToModify.getUserRole() %></p>
        <%
            }
        %>
    </div>
    <form action="/projetSB/UserModificationController" method="post">
        <h3>Nouvelles informations</h3>
        <label>Nouveau nom : </label>
        <input type="text" name="userNewLastName">

        <label>Nouveau prénom : </label>
        <input type="text" name="userNewName">

        <label>Nouveau email : </label>
        <input type="text" name="userNewEmail">

        <label>Nouvelle date de naissance : </label>
        <input type="date" name="userNewBirthdate">

        <%
            if(role.equals(Role.student)) {
        %>
            <label>Nouvelle classe : </label>
            <select name="userNewClassesId">
                <option value="">Ne pas changer la classe</option>
                <%
                    for(Classes classe : classesList) {
                %>
                <option value="<%= classe.getClassId() %>"><%= classe.getClassName() %></option>
                <%
                    }
                %>
            </select>

            <label>Nouvelle filière : </label>
            <select name="userNewMajorId">
                <option value="">Ne pas changer la filière</option>
                <%
                    for(Major major : majorList) {
                %>
                <option value="<%= major.getMajorId() %>"><%= major.getMajorName() %></option>
                <%
                    }
                %>
            </select>

        <%
            }
        %>

        <label>Nouveaux rôle : </label>
        <select name="userNewRole">
            <option value="">Ne pas changer le rôle</option>
            <option value=<%= Role.student %>>Étudiants</option>
            <option value="<%= Role.teacher %>">Enseignants</option>
            <option value="<%= Role.administrator %>">Administrateurs</option>
        </select>

        <input name="userId" value="<%= userToModify.getUserId() %>" style="display: none">
        <%
            }
        %>
        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p></br>
        <%
            }
        %>

        <button type="submit" onclick="confirmModify(event)">Modifier</button>
    </form>
</div>
</body>
<script>
    function confirmModify(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir modifier l'utilisateur ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
