<%@ page import="com.example.testspring.entities.Subjects" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 15/11/2024
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gestion des matières</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des matières</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Subjects> subjectList = (List<Subjects>) request.getAttribute("subjects");

        if (subjectList == null || subjectList.isEmpty()) {
    %>
    <h2>Il n'y a pas encore de matière</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche"></br></br>
    <form method="get">
        <table border="1">
            <th>Nom de la matière</th>
            <th>Selection</th>
            <%
                for (Subjects subject : subjectList) {
            %>
            <tr>
                <td><%= subject.getSubjectName() %></td>
                <td><input type="radio" name="subjectId" value="<%= subject.getSubjectId()%>" required></td>
            </tr>
            <%
                }
            %>
        </table>
        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p></br>
        <%
            }
        %>
        <button type="submit" formaction="/projetSB/SubjectModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/SubjectDeletionController" formmethod="post" onclick="confirmDelete(event)">Supprimer</button>
    </form>
    <%
        }
    %>
    <form action="/projetSB/SubjectCreationController" method="get">
        <button type="submit">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmDelete(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir supprimer la matière ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
