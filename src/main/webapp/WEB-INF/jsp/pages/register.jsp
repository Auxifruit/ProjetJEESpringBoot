<%@ page import="com.example.testspring.entities.Major" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Inscription</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<form action="/projetSB/RegisterController" method="post" onsubmit="validateForm(event)">
    <h1>Inscription</h1>
    <label for="firstName">Prénom :</label>
    <input type="text" name="firstName" id="firstName" placeholder="Prénom" required><br>

    <label for="lastName">Nom :</label>
    <input type="text" name="lastName" id="lastName" placeholder="Nom" required><br>

    <label for="email">Email :</label>
    <input type="email" name="email" id="email" placeholder="Email exemple@xyz.com" required><br>

    <label for="password">Mot de passe :</label>
    <input type="password" name="password" id="password" required><br>

    <label for="birthdate">Date de naissance :</label>
    <input type="date" name="birthdate" id="birthdate" required><br>

    <label for="role">Choix du role :</label>
    <select type="date" name="role" id="role" onchange="toggleMajorChoice()" required>
        <option value=<%= Role.student %> selected="selected">Étudiant</option>
        <option value="<%= Role.teacher %>">Enseignant</option>
    </select></br>

    <%
        List<Major> majorList =(List<Major>) request.getAttribute("majors");

        if(majorList != null && !majorList.isEmpty()) {
    %>
    <label for="major" class="majorChoice">Filière :</label>
    <select name="major" id="major" class="majorChoice">
        <option value="" selected="selected">Choisir une filière</option>
        <%
            for(Major major : majorList) {
        %>
        <option value="<%= major.getMajorId() %>"><%= major.getMajorName() %></option>
        <%
            }
        %>
    </select><br>
    <%
        }
    %>

    <button type="submit">S'inscrire</button>
    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p>
    <% } %>
    <% if (request.getAttribute("success") != null) { %>
    <p style="color: green;"><%= request.getAttribute("success") %></p>
    <% } %>
    <p>Pas encore de compte ? <a href="${pageContext.request.contextPath}/login">Connectez-vous.</a></p>
</form>
</body>
<script>
    function toggleMajorChoice() {
        const roleSelect = document.getElementById("role");
        const majorChoices = document.getElementsByClassName("majorChoice");
        const majorSelect = document.getElementById("major");

        if (roleSelect.value === "student") {
            for (let i = 0; i < majorChoices.length; i++) {
                majorChoices[i].style.display = "block";
            }
        } else {
            for (let i = 0; i < majorChoices.length; i++) {
                majorChoices[i].style.display = "none";
            }
            majorSelect.value = "";
        }
    }

    function validateForm(event) {
        const roleSelect = document.getElementById("role");
        const majorSelect = document.getElementById("major");
        if (roleSelect.value === "student" && majorSelect.value === "") {
            event.preventDefault(); // Empêche l'envoi du formulaire
            alert("Veuillez choisir une filière.");
        }
    }
</script>
</html>