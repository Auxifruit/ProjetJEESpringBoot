<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%--
Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 15/11/2024
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Assigner des étudiant</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Assignation d'étudiants à des classes</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Classes classe = (Classes) request.getAttribute("classe");

        List<Student> availableStudentList = (List<Student>) request.getAttribute("availableStudents");
        List<Student> participatingStudentList = (List<Student>) request.getAttribute("participatingStudentList");
        Map<Integer, Users> mapStudentIdUser = (Map<Integer, Users>) request.getAttribute("mapStudentIdUser");
        Map<Integer, Major> mapStudentIdMajor = (Map<Integer, Major>) request.getAttribute("mapStudentIdMajor");
        Map<Integer, Classes> mapStudentIdClass = (Map<Integer, Classes>) request.getAttribute("mapStudentIdClass");

        if (classe == null) {
    %>
    <h2>La classe n'existe pas</h2>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Informations de la classe :</h3>
        <p>Nom : <%= classe.getClassName() %> </p>
    </div>
    <form action="/projetSB/StudentClassesAssignationController" method="post">
        <h3>Étudiant(s) disponible(s) : </h3>
    <%
        if(availableStudentList == null || availableStudentList.isEmpty()) {
    %>
    <p>Aucun étudiant n'est disponible pour la classe</p>
    <%
        } else {
    %>
        <table border="1">
            <tr>
                <th>Nom de l'étudiant</th>
                <th>Prénom de l'étudiant</th>
                <th>Filière de l'étudiant</th>
                <th>Selection</th>
            </tr>
            <%
                for(Student student : availableStudentList) {
                    Users u = mapStudentIdUser.get(student.getStudentId());
            %>
            <tr>
                <td><%= u.getUserLastName()  %></td>
                <td><%= u.getUserName()  %></td>
                <td>
                    <% String majorName = null;
                        if(student.getMajorId() != null) {
                            majorName = mapStudentIdMajor.get(student.getStudentId()).getMajorName();
                        }
                        if(majorName == null) {
                    %>
                    Aucune
                    <%
                        } else {
                    %>
                    <%= majorName %>
                    <%
                        }
                    %>
                </td>
                <td><input type="radio" name="studentId" value="<%= student.getStudentId() %>"></td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="text" name="classesId" value="<%= classe.getClassId() %>" style="display: none">

        <button type="submit" onclick="confirmAction(event, 'assign')">Assigner</button>
    <%
        }
    %>
    </form>
    <form action="/projetSB/StudentClassesUnassignationController" method="post">
        <h3>Étudiant(s) participants(s) : </h3>
    <%
        if(participatingStudentList == null || participatingStudentList.isEmpty()) {
    %>
    <p>Aucune étudiant n'est dans la classe</p>
    <%
        } else {
    %>
        <table border="1">
            <tr>
                <th>Nom de l'étudiant</th>
                <th>Prénom de l'étudiant</th>
                <th>Filière de l'étudiant</th>
                <th>Selection</th>
            </tr>
            <%
                for(Student student : participatingStudentList) {
                    Users u = mapStudentIdUser.get(student.getStudentId());
            %>
            <tr>
                <td><%= u.getUserLastName()  %></td>
                <td><%= u.getUserName()  %></td>
                <td>
                    <% String majorName = null;
                        if(student.getMajorId() != null) {
                            majorName = mapStudentIdMajor.get(student.getStudentId()).getMajorName();
                        }
                        if(majorName == null) {
                    %>
                    Aucune
                    <%
                    } else {
                    %>
                    <%= majorName %>
                    <%
                        }
                    %>
                </td>
                <td><input type="radio" name="studentId" value="<%= student.getStudentId() %>"></td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="text" name="classesId" value="<%= classe.getClassId() %>" style="display: none">

        <% String messageErreur = (String) request.getAttribute("erreur");
            if(messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
                }
        %>
        <button type="submit" onclick="confirmAction(event, 'unassign')">Désassigner</button>
        <%
            }
        %>
    </form>
    <%
        }
    %>
</div>
</body>
<script>
    function confirmAction(event, action) {
        let confirmationMessage = '';

        if (action === 'assign') {
            confirmationMessage = "Êtes-vous sûr de vouloir assigner cet étudiant à cette classe ?";
        } else if (action === 'unassign') {
            confirmationMessage = "Êtes-vous sûr de vouloir désassigner cet étudiant à cette classe ?";
        } else {
            confirmationMessage = "Êtes-vous sûr de vouloir effectuer cette action ?";
        }

        const confirmation = confirm(confirmationMessage);

        if (!confirmation) {
            event.preventDefault(); // Annule l'envoi du formulaire si l'utilisateur annule
        }
    }
</script>
</html>
