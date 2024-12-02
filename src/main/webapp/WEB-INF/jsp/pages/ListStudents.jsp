<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%
  List<Map<String, Object>> etudiants = (List<Map<String, Object>>) request.getAttribute("users");
  List<String> availableClasses = (List<String>) request.getAttribute("availableClasses"); // Liste des classes à partir du backend.
  if (etudiants == null) {
    etudiants = new ArrayList<>();
  }
  if (availableClasses == null) {
    availableClasses = new ArrayList<>();
  }
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/ListStudents.css">
<!-- Formulaire de recherche -->
<form>
  <div style="margin-bottom: 20px;">
    <!-- Sélecteur de critère de recherche -->
    <label for="filterCriteria">Rechercher par :</label>
    <select id="filterCriteria">
      <option value="all">Tous</option>
      <option value="prenom">Prénom</option>
      <option value="nom">Nom</option>
      <option value="email">Email</option>
    </select>

    <!-- Barre de recherche -->
    <input
            type="text"
            id="searchInput"
            onkeyup="filterTable()"
            placeholder="Entrez votre recherche"
            style="width: 300px; padding: 5px;"
    >
  </div>
</form>

<!-- Formulaire de filtrage par classe -->
<form>
  <div style="margin-bottom: 20px;">
    <label for="classSelector">Filtrer par classe :</label>
    <select id="classSelector" onchange="filterTable()">
      <option value="all">Toutes les classes</option>
      <%
        for (String classe : availableClasses) {
      %>
      <option value="<%= classe %>"><%= classe %></option>
      <%
        }
      %>
    </select>
  </div>
</form>

<table id="myTable">
  <thead>
  <tr>
    <th>Prénom</th>
    <th>Nom</th>
    <th>Email</th>
    <th>Date de Naissance</th>
    <th>Classe</th>
  </tr>
  </thead>
  <tbody>
  <%
    if (etudiants.isEmpty()) {
  %>
  <tr>
    <td colspan="5">Aucun étudiant trouvé</td>
  </tr>
  <%
  } else {
    for (Map<String, Object> etudiant : etudiants) {
  %>
  <tr>
    <td><%= etudiant.get("userName") %></td>
    <td><%= etudiant.get("userLastName") %></td>
    <td><%= etudiant.get("userEmail") %></td>
    <td><%= etudiant.get("userBirthdate") %></td>
    <td><%= etudiant.get("className") %></td>
  </tr>
  <%
      }
    }
  %>
  </tbody>
</table>

<script>
  function filterTable() {
    // Récupérer la valeur de la recherche, le critère sélectionné et la classe
    const input = document.getElementById('searchInput').value.toLowerCase();
    const filterCriteria = document.getElementById('filterCriteria').value;
    const selectedClass = document.getElementById('classSelector').value;
    const table = document.getElementById('myTable');
    const rows = table.getElementsByTagName('tr');

    // Parcourir toutes les lignes du tableau et les filtrer
    for (let i = 1; i < rows.length; i++) { // Commencer à 1 pour ignorer l'en-tête du tableau
      const cells = rows[i].getElementsByTagName('td');
      let match = false;

      // Filtrage basé sur le critère de recherche
      if (filterCriteria === 'prenom' && cells[0].textContent.toLowerCase().includes(input)) {
        match = true;
      } else if (filterCriteria === 'nom' && cells[1].textContent.toLowerCase().includes(input)) {
        match = true;
      } else if (filterCriteria === 'email' && cells[2].textContent.toLowerCase().includes(input)) {
        match = true;
      } else if (filterCriteria === 'all') {
        match = Array.from(cells).some(cell => cell.textContent.toLowerCase().includes(input));
      }

      // Filtrage basé sur la classe
      const classMatch = selectedClass === 'all' || cells[4].textContent === selectedClass;

      // Afficher ou masquer la ligne
      rows[i].style.display = match && classMatch ? '' : 'none';
    }
  }
</script>