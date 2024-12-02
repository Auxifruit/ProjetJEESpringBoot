<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.example.testspring.entities.*" %><%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 19/11/2024
  Time: 15:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Emploi du temps</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Emploi du temps : </h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null) {
            response.sendRedirect("index");
            return;
        }

        Role role = connectedUser.getUserRole();
        boolean isTeacher = Role.teacher.equals(role);

        Map<LocalDate, List<Lesson>> lessonList = (Map<LocalDate, List<Lesson>>) request.getAttribute("lessonList");
        Map<LocalDate, List<Lesson>> pastLessons = (Map<LocalDate, List<Lesson>>) request.getAttribute("pastLessons");
        Map<LocalDate, List<Lesson>> futureLessons = (Map<LocalDate, List<Lesson>>) request.getAttribute("futureLessons");

        Map<Integer, Users> mapLessonIdTeacher = (Map<Integer, Users>) request.getAttribute("mapLessonIdTeacher");
        Map<Integer, Course> mapLessonIdCourse = (Map<Integer, Course>) request.getAttribute("mapLessonIdCourse");
        Map<Integer, List<Classes>> mapLessonIdClassesList =  (Map<Integer, List<Classes>>) request.getAttribute("mapLessonIdClassesList");

        request.setAttribute("mapLessonIdClassesList", mapLessonIdClassesList);

        if(lessonList == null || lessonList.isEmpty()) {
    %>
    <h2>L'emploi du temps est vide</h2>
    <%
        } else {
            LocalDate today = LocalDate.now();

            for (Map.Entry<LocalDate, List<Lesson>> entry : lessonList.entrySet()) {
                if (entry.getKey().isBefore(today)) {
                    pastLessons.put(entry.getKey(), entry.getValue());
                } else {
                    futureLessons.put(entry.getKey(), entry.getValue());
                }
            }
    %>
    <div id="OldInfos">
        <h2>Cours passés :</h2>
        <div style="display: none" id="pastCoursesTable">
            <%
                if(pastLessons == null || pastLessons.isEmpty()) {
            %>
            <h2>Pas de cours passés</h2>
            <%
                } else {
                    for(Map.Entry<LocalDate, List<Lesson>> entry : pastLessons.entrySet()) {
                        LocalDate day = entry.getKey();
                        List<Lesson> lessons = entry.getValue();
            %>
            <h3><%= day.format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")) %></h3>

            <table border="1">
                <tr>
                    <th>Nom du cours</th>
                    <% if(!isTeacher) {
                    %>
                        <th>Nom prénom de l'enseignant</th>
                    <%
                    }
                    %>
                    <th>Heure de début</th>
                    <th>Heure de fin</th>
                    <th>Classe participante</th>
                </tr>

                <%
                    for(Lesson lesson : lessons) {
                        LocalDateTime startDate = lesson.getLessonStartDate().toLocalDateTime();
                        LocalDateTime endDate = lesson.getLessonEndDate().toLocalDateTime();
                        List<Classes> participantClass = mapLessonIdClassesList.get(lesson.getLessonId());
                %>
                <tr>
                    <td>
                        <%
                            if(participantClass.size() > 1) {
                        %>
                        <div style="color: red">CM</div>
                        <%
                            } else {
                        %>
                        <div style="color: blue">TD</div>
                        <%
                            }
                            Course course = mapLessonIdCourse.get(lesson.getLessonId());
                            if(course != null) {
                        %>
                            <%= course.getCourseName() %>
                        <%
                            } else {
                        %>
                            Pas de cours assosié
                        <%
                            }
                        %>
                    </td>
                    <% if(!isTeacher) {
                    %>
                    <td>
                    <%
                            Users teacher = mapLessonIdTeacher.get(lesson.getLessonId());
                            if(teacher != null) {
                    %>
                        <%= teacher.getUserLastName() + " " + teacher.getUserName() %>
                    <%
                            } else {
                    %>
                        Pas d'enseignant pour cette séance
                    <%
                            }
                        }
                    %>
                    </td>
                    <td><%= startDate.format(DateTimeFormatter.ofPattern("HH:mm")) %></td>
                    <td><%= endDate.format(DateTimeFormatter.ofPattern("HH:mm")) %></td>
                    <td>
                        <%
                            if(participantClass == null || participantClass.isEmpty()) {
                        %>
                        <p>Aucune classe ne participe au cours</p>
                        <%
                        } else {
                            for(Classes classe : participantClass) {
                        %>
                        <%= classe.getClassName() + " "%>
                        <%
                                }
                            }
                        %>
                    </td>
                </tr>
                <%
                    }
                %>

            </table>
            <%
                }
                }
            %>
        </div></br></br>
        <button onclick="toggleDisplay()">Afficher ou non les cours passés</button>
    </div>
    <div id="OldInfos">
        <h2>Cours à venir :</h2>
            <%
                if(futureLessons == null || futureLessons.isEmpty()) {
            %>
                <h2>Pas de cours à venir</h2>
            <%
                } else {
                    for(Map.Entry<LocalDate, List<Lesson>> entry : futureLessons.entrySet()) {
                        LocalDate day = entry.getKey();
                        List<Lesson> lessons = entry.getValue();
            %>

            <h3><%= day.format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy")) %></h3>

            <table border="1">
                <tr>
                    <th>Nom du cours</th>
                    <% if(!isTeacher) {
                    %>
                    <th>Nom prénom de l'enseignant</th>
                    <%
                        }
                    %>
                    <th>Heure de début</th>
                    <th>Heure de fin</th>
                    <th>Classe participante</th>
                </tr>

                <%
                    for(Lesson lesson : lessons) {
                        LocalDateTime startDate = lesson.getLessonStartDate().toLocalDateTime();
                        LocalDateTime endDate = lesson.getLessonEndDate().toLocalDateTime();
                        List<Classes> participantClass = mapLessonIdClassesList.get(lesson.getLessonId());
                %>
                <tr>
                    <td>
                        <%
                            if(participantClass.size() > 1 ) {
                        %>
                        <div style="color: red">CM</div>
                        <%
                            } else {
                        %>
                        <div style="color: blue">TD</div>
                        <%
                            }
                            Course course = mapLessonIdCourse.get(lesson.getLessonId());
                            if(course != null) {
                        %>
                        Pas de cours assosié
                        <%
                            } else {
                        %>
                        <%= course.getCourseName() %>
                        <%
                            }
                        %>
                    </td>
                    <% if(!isTeacher) {
                        Users teacher = mapLessonIdTeacher.get(lesson.getLessonId());
                    %>
                    <td><%= teacher.getUserLastName() + " " + teacher.getUserName() %></td>
                    <%
                        }
                    %>
                    <td><%= startDate.format(DateTimeFormatter.ofPattern("HH:mm")) %></td>
                    <td><%= endDate.format(DateTimeFormatter.ofPattern("HH:mm")) %></td>
                    <td>
                        <%
                            if(participantClass == null || participantClass.isEmpty()) {
                        %>
                        <p>Aucune classe ne participe au cours</p>
                        <%
                            } else {
                                for(Classes classe : participantClass) {
                        %>
                        <%= classe.getClassName() + " "%>
                        <%
                                }
                            }
                        %>
                    </td>
                </tr>
                <%
                    }
                %>

            </table>
                <%
                    }
                }
            %>
    </div>
    <%
        }
    %>
</div>
</body>
<script>
    function toggleDisplay() {
        const table = document.getElementById('pastCoursesTable');
        if (table.style.display === 'none') {
            table.style.display = 'table';
        } else {
            table.style.display = 'none';
        }
    }
</script>
</html>
