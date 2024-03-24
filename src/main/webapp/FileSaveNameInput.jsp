<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Name Reader</title>
</head>
<body>
<form action = "SaveToDatabase.jsp" method="post">
    <label for="FileNameInput">Please enter the file name </label>
    <p>Please end the file name in either .csv or .json depending on what you want to use</p>
    <p>note that it will default to csv if no file type is specified</p>
    <input type="text" id="FileNameInput" name="fileNameInput">
    <button type="submit">submit name</button>
</form>
</body>
</html>
