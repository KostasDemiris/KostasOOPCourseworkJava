<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<body>
<%
    List<String> columnNames = (List<String>) request.getAttribute("columnNames");
    List<String> patient = (List<String>) request.getAttribute("result");
    if (!patient.isEmpty())
    // This adjustment has been made because it also prints the column names inside of patients, so an empty acc has
    // length one
    {
%>
<ul>
        <%
        String ID = patient.getFirst();
        String href = "/patientProfile.html?patientID=" + ID; // Patient is now a query parameter
    %>
    <li><a href="<%=href%>"><%="You can access the patient's profile here"%></a>
            <%
        for (int index = 0; index < patient.size(); index++){
    %>
    <li><%="patient " + columnNames.get(index) + " is " + patient.get(index)%></li>
        <%
      }
    } else
    {%>
    <p>Nothing Founding...</p>
        <%}%>

</body>
</html>
