<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<%
    List<String> columnNames = (List<String>) request.getAttribute("columnNames");
    List<String> households = (List<String>) request.getAttribute("result");
    for (String household: households){
        // household is a string of elements with each being seperated with ",": first is ID, then each following one
        // is a
        String[] householdInfo = household.split(",");
        String householdAddress = householdInfo[0];
        List<String> Names = Arrays.stream(householdInfo).skip(1).map(el -> el.split(": ")[1]).toList();
        List<String> patientProfiles = Arrays.stream(householdInfo).map(el -> el.split(": ")[0])
                .skip(1).map(id -> "/patientProfile.html?patientID=" + id).toList();
        if (Names.size() == 1){
%>
<p>For the household living at "<b><%=householdAddress%></b>", there is 1 person living there: </p>
<%
        }
        else{
%>
<p>For the household living at "<b><%=householdAddress%></b>", there are <%=Names.size()%> people living there: </p>
<%}
        for (int index = 0; index < Names.size(); index++){
    %>
<li><a href="<%=patientProfiles.get(index)%>"> <%=Names.get(index)%></a>
    <% }
    %><br/><br/><%
    }
%>

</body>
</html>
