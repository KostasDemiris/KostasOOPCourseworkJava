<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:include page="header.jsp"/>
    <html>
    <head>
    <title>File Name Reader</title>
</head>
<body>
<form action = "ReadNewFile.jsp" method="post">
    <label for="FileNameInput">Please enter the file name </label>
    <p>Please correctly enter the type of file you are ending, so .csv or .json</p>
    <p>note that it will default to ignoring the read if not specified</p>
    <input type="text" id="FileNameInput" name="fileNameInput">
    <input type="checkbox" id="mergeCheckbox" name="merge" value="true">
    <label for="mergeCheckbox">Merge file?</label>
    <button type="submit">submit name</button>
    <%
        // This asks whether to merge the file into the current database, or whether to replace it.
        // It defaults to replacing it
    %>
</form>
<jsp:include page="footer.jsp"/>
</body>
</html>
