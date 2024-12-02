<%@ page import="com.example.testspring.entities.Subjects" %>
<%@ page import="java.util.List" %>
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
    <title>Création de matière</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/showTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Création d'une nouvelle matière</h1>
    <div id="OldInfos">
        <h3>Matière existante : </h3>
        <%
            Users connectedUser = (Users) session.getAttribute("connectedUser");
            if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
                response.sendRedirect("index");
                return;
            }

            List<Subjects> subjectList = (List<Subjects>) request.getAttribute("subjects");

            if (subjectList == null || subjectList.isEmpty()) {
        %>
        <p>Il n'y a pas encore de matière</p>
        <%
        } else {
        %>
        <button onclick="toggleTable()">Afficher/Masquer le tableau</button></br></br>
        <table border="1" id="existingTable" style="display: table">
            <th>Nom de la matière</th>
            <%
                for (Subjects subject : subjectList) {
            %>
            <tr>
                <td><%= subject.getSubjectName() %>
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
    <form action="/projetSB/SubjectCreationController" method="post">
        <h3>Nouvelle matière :</h3>
        <label>Nom de la nouvelle matière : </label>
        <input type="text" name="newSubject" required/>
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
        const confirmation = confirm("Êtes-vous sûr de vouloir créer la matière ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
