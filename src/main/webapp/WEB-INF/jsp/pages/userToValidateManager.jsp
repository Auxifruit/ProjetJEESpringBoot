<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 07/11/2024
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%@ page import="com.example.testspring.entities.Userstovalidate" %>
<%@ page import="com.example.testspring.entities.Major" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Liste utilisateurs à valider</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>List des utilisateurs à valider</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Userstovalidate> userstovalidateList = (List<Userstovalidate>) request.getAttribute("usersToValidate");
        Map<Integer, Major> mapUserToValidateIdMajor = (Map<Integer, Major>) request.getAttribute("mapUserToValidateIdMajor");

        if(userstovalidateList == null || userstovalidateList.isEmpty()) {
    %>
    <h2>Pas d'utilisateurs à valider</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche">
    </br></br>

    <form method="post" id="userForm">
        <table border="1px">
            <tr>
                <th>Nom de l'utilisateur</th>
                <th>Prenom de l'utilisateur</th>
                <th>Email de l'utilisateur</th>
                <th>Date de naissance de l'utilisateur</th>
                <th>Rôle de l'utilisateur</th>
                <th>Filière de l'utilisateur</th>
                <th>Selection</th>
            </tr>
            <%
                for (Userstovalidate u : userstovalidateList) {
            %>
            <tr>
                <td><%= u.getUserToValidateLastName() %></td>
                <td><%= u.getUserToValidateName() %></td>
                <td><%= u.getUserToValidateEmail() %></td>
                <td><%= u.getUserToValidateBirthdate() %></td>
                <td><%= u.getUserToValidateRole().toString() %></td>
                <td>
                <%
                    Major major = mapUserToValidateIdMajor.get(u.getUserToValidateMajorId());
                    if(major == null) {
                %>
                    Pas de filière
                <%
                } else {
                %>
                    <%= major.getMajorName() %>
                <%
                    }
                %>
                </td>
                <td><input type="radio" name="userToValidateId" value="<%= u.getUserToValidateId() %>" required></td>
            </tr>
            <%
                }
            %>
        </table>

        <% String messageErreur = (String) request.getAttribute("erreur");
            if(messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p></br>
        <%
            }
        %>

        <button type="submit" onclick="confirmSubmission(document.getElementById('userForm'), 'validate')">Valider</button>
        <button type="submit" onclick="confirmSubmission(document.getElementById('userForm'), 'refuse')">Refuser</button>
    </form>
    <%
        }
    %>
</div>
</body>
<script>
    function confirmSubmission(form, action) {
        const confirmation = confirm("Êtes-vous sûr de vouloir " + (action === 'validate' ? "valider" : "refuser") + " cet utilisateur ?");
        if (confirmation) {
            form.action = action === 'validate' ? '/projetSB/ValidateUserController' : '/projetSB/RefuseUserController';
            form.submit();
        }
    }
</script>
</html>
