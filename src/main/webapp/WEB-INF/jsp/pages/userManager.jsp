<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.testspring.entities.*" %>
<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 07/11/2024
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <title>Liste utilisateurs</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des utilisateurs</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Users> userList = (List<Users>) request.getAttribute("users");
        Map<Integer, Student> mapUserIdStudent = (Map<Integer, Student>) request.getAttribute("mapUserIdStudent");
        Map<Integer, Classes> mapStudentIdClass = (Map<Integer, Classes>) request.getAttribute("mapStudentIdClass");
        Map<Integer, Major> mapStudentIdMajor = (Map<Integer, Major>) request.getAttribute("mapStudentIdMajor");

        String selectedRole = (String) request.getAttribute("roleFilter");
        Role roleFilter = null;
        if(selectedRole != null && !selectedRole.isEmpty()) {
            roleFilter = Role.valueOf(selectedRole);
        }

        if(userList == null || userList.isEmpty()) {
    %>
    <h2>Il n'y a pas d'utilisateur</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche">
    </br></br>
    <form action="UserManagerController" method="get" >
        <label><b>Filtrer par : </b></label>
        <select name="roleFilter">
            <option value=<%= Role.student %> <%= roleFilter != null && roleFilter.equals(Role.student) ? "selected" : "" %>>Étudiants</option>
            <option value=<%= Role.teacher %> <%= roleFilter != null && roleFilter.equals(Role.teacher) ? "selected" : "" %>>Enseignants</option>
            <option value=<%= Role.administrator %> <%= roleFilter != null && roleFilter.equals(Role.administrator) ? "selected" : "" %>>Administrateurs</option>
        </select>
        <button type="submit">Valider</button>
    </form>
    <%
        boolean isStudentSelected = Role.student.equals(roleFilter);
    %>
    <form method="get">
    <table>
        <tr>
            <th>Nom de l'utilisateur</th>
            <th>Prenom de l'utilisateur</th>
            <th>Email de l'utilisateur</th>
            <th>Date de naissance de l'utilisateur</th>
            <%
                if (isStudentSelected) {
            %>
            <th>Classe</th>
            <th>Filière</th>
            <%
                }
            %>
            <th>Rôle de l'utilisateur</th>
            <th>Selection</th>
        </tr>
        <%
            for (Users u : userList) {
        %>
        <tr>
            <td><%= u.getUserLastName() %></td>
            <td><%= u.getUserName() %></td>
            <td><%= u.getUserEmail() %></td>
            <td><%= u.getUserBirthdate() %></td>
            <%
                if (isStudentSelected) {
                    Student student = mapUserIdStudent.get(u.getUserId());

                    Classes studentClass = mapStudentIdClass.get(student.getClassId());
                    Major studentMajor = mapStudentIdMajor.get(student.getMajorId());
            %>
            <td>
            <%
                    if(studentClass != null) {
            %>
            <%= studentClass.getClassName() %>
            <%
                    } else {
            %>
                Sans classe
            <%
                }
            %>
            </td>
            <td>
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
            </td>
            <%
                }
            %>
            <td><%= u.getUserRole() %></td>
            <td><input type="radio" name="userId" value="<%= u.getUserId() %>" required></td>
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
        <button type="submit" formaction="/projetSB/UserModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/UserDeletionController" onclick="confirmDelete(event)">Supprimer</button>
        <%
            if(roleFilter != null && !roleFilter.equals(Role.administrator)) {
        %>
        <button type="submit" formaction="/projetSB/UserScheduleController" formmethod="get">Voir l'emploi du temps de l'utilisateur</button>
        <%
            }
        %>
        <%
            if(roleFilter != null && roleFilter.equals(Role.student)) {
        %>
        <button type="submit" formaction="/projetSB/StudentGradeReportController">Voir les notes de l'utilisateur</button>
        <%
            }
        %>
    </form>
    <%
        }
    %>
    <form action="/projetSB/UserCreationController" method="get">
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
