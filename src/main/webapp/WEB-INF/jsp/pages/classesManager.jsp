<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Classes" %>
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
    <title>Gestion des classes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des classes</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Classes> classList = (List<Classes>) request.getAttribute("classes");

        if (classList == null || classList.isEmpty()) {
    %>
    <h2>Il n'y a pas encore de classe</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche">
    </br></br>
    <form method="get">
        <table border="1">
            <th>Nom de la classe</th>
            <th>Selection</th>
            <%
                for (Classes classe : classList) {
            %>
            <tr>
                <td><%= classe.getClassName() %></td>
                <td><input type="radio" name="classesId" value="<%= classe.getClassId()%>" required></td>
            </tr>
            <%
                }
            %>
        </table>

        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
            }
        %>

        <button type="submit" formaction="/projetSB/ClassesModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/ClassesDeletionController" formmethod="post" onclick="confirmDelete(event)">Supprimer</button>
        <button type="submit" formaction="/projetSB/StudentClassesManagerController">Assigner un ou plusieurs étudiants à la classe</button>
    </form>
    <%
        }
    %>
    <form action="/projetSB/ClassesCreationController" method="get">
        <button type="submit">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmDelete(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir supprimer la classe ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
