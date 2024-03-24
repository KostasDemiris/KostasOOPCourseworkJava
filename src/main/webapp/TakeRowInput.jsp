<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TakeInRow</title>
</head>
<body>
<%
    ArrayList<String> columnNames = (ArrayList<String>) request.getAttribute("columnNames");
    int columnNumber = columnNames.size();
    ArrayList<String> values;
    try{
        values = new ArrayList<>((ArrayList<String>) request.getAttribute("rowData"));
        // If editing a column, this will have pre-existing data, if adding it won't
    }
    catch (NullPointerException e){
        values = new ArrayList<>();
        for (int index = 0; index < columnNumber; index++) {values.add("");}
    }
    %>
<h3>Please enter the data</h3>
<form action="/TakeRowInput.html?actionName<%=request.getAttribute("actionNamez")%>" method="post">
<%
    for (int index = 0; index < columnNumber; index++)
    {
%>

<label>
    <p><%=columnNames.get(index)%></p>
    <input type="text" name="input<%= columnNames.get(index) %>" value="<%=values.get(index) %>">
</label><br>

<%
    }
%>
    <input type="hidden" name="actionName" value="<%=request.getAttribute("actionName")%>">
    <input type = submit value = "Submit">
    </form>

</body>
</html>
