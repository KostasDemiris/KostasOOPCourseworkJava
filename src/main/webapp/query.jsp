<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Query Section</title>
</head>
<body>
<h1>This the Query Database section!</h1>
<div class="main">
    <h2>Select your query:</h2>
    <form id="QuerySelect" method="POST" onsubmit="searchForQuery()">
        <label for="QuerySelection">Select Query Type: </label>
        <select id="QuerySelection">
            <option value = "/queryDatabase.html?actionType=getYoungest"> Search for youngest </option>
            <option value = "/queryDatabase.html?actionType=getOldest"> Search for oldest </option>
            <option value = "/queryDatabase.html?actionType=getHouseholds"> Get Households</option>
        </select>
        <input type="submit" value="Go" class="button">set query type
    </form>

    <script>
        function searchForQuery() {
            // Gets the value of the selected option
            var selectedValue = document.getElementById("QuerySelection").value;
            // Updates the action attribute of the form
            document.getElementById("QuerySelect").setAttribute("action", selectedValue);
        }
    </script>
</div>
</body>
</html>
