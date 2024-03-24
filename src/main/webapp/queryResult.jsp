<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h1>Query Result</h1>
  <%
    String queryDataType = (String) request.getAttribute("queryDataType");
    switch (queryDataType){
      case "singlePatient": {
  %><jsp:include page="/displaySinglePatient.jsp"/><%
    break;
  }
  case "patientList": {
%><jsp:include page="/displayPatientList.jsp"/><%
    break;
  }
  case "HouseholdData" :
  {
  %><jsp:include page="/displayHouseholds.jsp"/><%
    break;
  }

  default: {
  %>
  <h5>No Query Selected</h5>
  <%
      break;
    }}%>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
