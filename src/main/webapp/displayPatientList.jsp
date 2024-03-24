<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    List<String> columnNames = (List<String>) request.getAttribute("columnNames");
    List<String> patients = (List<String>) request.getAttribute("result");
    if (patients.size() > 0)
    // This adjustment has been made because it also prints the column names inside of patients, so an empty acc has
    // length one
    {
%>
<ul>
    <%
    %>
    <li><%=columnNames.toString()%><br/><br/></li>
    <%

        for (String patient : patients)
        {
            String ID = patient.split(",")[0];
            String rest = Arrays.stream(patient.split(",")).skip(1).collect(Collectors.joining(", "));
            String href = "/patientProfile.html?patientID=" + ID; // Patient is now a query parameter
    %>
    <li><a href="<%=href%>"><%="ID is: " + ID%></a>
    <li><%=rest%><br/><br/></li>
    <% }
    } else
    {%>
    <p>Nothing found</p>
    <%}%>
</ul>
</body>
</html>
