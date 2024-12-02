<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Major" %>
<%@ page import="com.example.testspring.entities.Role" %>
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
    <title>Création de filière</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/showTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Création d'une nouvelle filière</h1>
    <div id="OldInfos">
        <h3>Filière existante : </h3>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Major> majorList = (List<Major>) request.getAttribute("majors");

        if (majorList == null || majorList.isEmpty()) {
    %>
    <p>Il n'y a pas encore de filière</p>
    <%
    } else {
    %>
        <button onclick="toggleTable()">Afficher/Masquer le tableau</button></br></br>
        <table border="1" id="existingTable" style="display: table">
            <th>Nom de la filière</th>
            <%
                for (Major major : majorList) {
            %>
            <tr>
                <td><%= major.getMajorName() %></td>
            </tr>
            <%
                }
            %>
        </table>
        <%
            }
        %>
    </div>
    <form action="/projetSB/MajorCreationCreation" method="post">
        <h3>Nouvelle filière :</h3>
        <label>Choix du nom de la filière : </label>
        <input type="text" name="newMajor" required/>
        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %>
        </p>
        <%
            }
        %>
        <button type="submit" onclick="confirmCreate(event)">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmCreate(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir créer la filière ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
