<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="com.example.testspring.entities.Role" %>

<%
  Users connectedUser = (Users) session.getAttribute("connectedUser");
  if (connectedUser == null) {
    response.sendRedirect("index");
    return;
  }

  Classes studentClass = (Classes) request.getAttribute("studentClass");
  Major studentMajor = (Major) request.getAttribute("studentMajor");

%>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>Informations personnelles</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <style>
    label { font-weight: bold; }
    input[type="text"], input[type="password"], input[type="date"] {
      margin-bottom: 10px;
      width: 100%;
      padding: 5px;
    }
    .btn { padding: 8px 12px; cursor: pointer; margin-top: 10px; }
    .btn-modify { background-color: #4CAF50; color: white; }
    .btn-cancel { background-color: #f44336; color: white; }
  </style>
  <script>
    // Utilisation des données utilisateur dans JavaScript
    let originalValues = {
      email: "<%= connectedUser.getUserEmail() %>",
      nom: "<%= connectedUser.getUserLastName() %>",
      prenom: "<%= connectedUser.getUserName() %>",
      dateNaissance: "<%= connectedUser.getUserBirthdate() %>",
      motDePasse: "<%= connectedUser.getUserPassword() %>"
    };

    function toggleEditMode(isEditable) {
      document.getElementById("email").readOnly = !isEditable;
      document.getElementById("nom").readOnly = !isEditable;
      document.getElementById("prenom").readOnly = !isEditable;
      document.getElementById("motDePasse").readOnly = !isEditable;
      document.getElementById("dateNaissance").readOnly = !isEditable;
      document.getElementById("btnModify").style.display = isEditable ? "none" : "inline";
      document.getElementById("btnSave").style.display = isEditable ? "inline" : "none";
      document.getElementById("btnCancel").style.display = isEditable ? "inline" : "none";
    }

    function cancelEdit() {
      document.getElementById("email").value = originalValues.email;
      document.getElementById("nom").value = originalValues.nom;
      document.getElementById("prenom").value = originalValues.prenom;
      document.getElementById("motDePasse").value = originalValues.motDePasse;
      document.getElementById("dateNaissance").value = originalValues.dateNaissance;
      toggleEditMode(false);
    }
  </script>
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
  <h1>Mes informations</h1>
  <%
    if(user.getUserRole().equals(Role.student)) {
  %>
  <div id="OldInfos">
    <h3>Informations étudiantes</h3>
    <p>Classe :
      <%
        if(studentClass != null) {
      %>
      <%= studentClass.getClassName() %>
      <%
      } else {
      %>
      Aucune
      <%
        }
      %>
    </p>
    <p>Filière :
      <%
        if(studentMajor != null) {
      %>
      <%= studentMajor.getMajorName() %>
      <%
      } else {
      %>
      Aucune
      <%
        }
      %>
    </p>
  </div>
  <%
    }
  %>
  <form action="/projetSB/EditInformationsController" method="post">
    <h3>Informations personnelles</h3>
    <!-- Champ caché pour l'ID utilisateur -->
    <input type="hidden" id="userId" name="userId" value="<%= user.getUserId() %>">

    <label>Email :</label>
    <input type="text" id="email" name="email" value="<%= user.getUserEmail() %>" readonly>

    <label>Nom :</label>
    <input type="text" id="nom" name="nom" value="<%= user.getUserLastName() %>" readonly>

    <label>Prénom :</label>
    <input type="text" id="prenom" name="prenom" value="<%= user.getUserName() %>" readonly>

    <label>Date de naissance :</label>
    <input type="date" id="dateNaissance" name="dateNaissance" value="<%= user.getUserBirthdate() %>" readonly>

    <label>Mot de passe :</label>
    <div>
      <input type="password" id="motDePasse" name="motDePasse" value="<%= user.getUserPassword() %>" readonly>
    </div>

    <!-- Boutons -->
    <button type="button" id="btnModify" onclick="toggleEditMode(true)">Modifier</button>
    <button type="submit" id="btnSave" style="display: none;">Enregistrer</button>
    <button type="button" id="btnCancel" style="display: none;" onclick="cancelEdit()">Annuler</button>
  </form>
</div>
</body>
</html>