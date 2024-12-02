<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 22/11/2024
  Time: 00:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Note Enregistrée</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
        }

        .container {
            width: 80%;
            margin: 20px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: #f5f5f5;
            text-align: center;
        }

        .message {
            color: #32cd32;
            font-size: 24px;
            margin: 40px 0;
        }

        .return-button {
            padding: 10px 20px;
            background-color: #a0c4ff;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
        }

        .return-button:hover {
            background-color: #4a90e2;
        }
    </style>
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div class="container">
    <div class="message">
        <p>✓ La note a bien été enregistrée</p>
    </div>

    <a href="/projetSB/EntryNoteController" class="return-button">
        Insérer une nouvelle note
    </a>
</div>
</body>
</html>