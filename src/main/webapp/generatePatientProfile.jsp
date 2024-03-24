<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <jsp:include page="/meta.jsp"/>
    <title>Patient Data</title>
</head>
<body>
<div class="main">
    <h1>Details</h1>
    <%
        List<String> details = (List<String>) request.getAttribute("patientDetails");
        List<String> columnNames = (List<String>) request.getAttribute("columnNames");


        if (details.size() !=0)
        {
    %>
    <ul>
        <%
            for (int index = 0; index < details.size(); index++)
            {
        %>
        <li><%="Column " + columnNames.get(index) + " is:  " + details.get(index)%></li>
        <% }
        } else
        {%>
        <p>Nothing found</p>
        <%}%>
    </ul>
    <p><a href="DeleteRow.html?patientID=<%=details.getFirst()%>">Delete this row</a></p>
    <p><a href="TakeRowInput.html?actionName=editRow&patientID=<%=details.getFirst()%>">Edit this row</a></p>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>