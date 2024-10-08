<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
  <jsp:include page="/meta.jsp"/>
  <title>Patient Data App</title>
  <script>
    function revealFileNameInput(){
      document.getElementById("FileNameInput").style.display = 'block';
    }
  </script>
</head>
<body>
<jsp:include page="/header.jsp"/>
<div class="main">
  <h2>Patients:</h2>
  <ul>
    <%
      List<String> patients = (List<String>) request.getAttribute("patientNames");
      List<String> patientIDs = (List<String>) request.getAttribute("patientIDs");
      for (int index = 0; index < patients.size(); index++)
      {
        String href = "/patientProfile.html?patientID=" + patientIDs.get(index); // Patient is now a query parameter
    %>
    <li><a href="<%=href%>"><%=patients.get(index)%></a>
    </li>
    <% } %>
  </ul>
</div>
<%
  request.setAttribute("actionName", "addRow");
%>

<button onclick="revealFileNameInput()">Save database to file</button>
<div id="FileNameInput" style="display: none">
  <jsp:include page="FileSaveNameInput.jsp"/>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
