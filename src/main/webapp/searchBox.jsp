<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>this is a search box that gets input from the user</title>
</head>
<body>
<div class="main">
    <h1>Search for <%request.getParameter("searchActionName");%></h1>
    <form method="POST" action="<%= request.getParameter("searchAction") %>">
        <label>
            <input type="text" name="searchstring" placeholder="Enter search keyword here"/>
        </label>
        <input type="submit" value="Search"/>
    </form>
</div>
</body>
</html>
