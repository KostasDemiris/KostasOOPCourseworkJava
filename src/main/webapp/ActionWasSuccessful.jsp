<%--
  This JSP is added into the header, and is usally empty, unless there was a redirection to this page by another one
  after an action in which case it'll display successful or unsuccessful...
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Was the last action successful</title>
</head>
<body>

<%
    String actionSuccessText;
    switch (response.getStatus()){
        case (200):
            actionSuccessText = "null";
            break;
        case (-100):
            actionSuccessText = "unsuccessful";
            break;
        case (100):
            actionSuccessText = "successful";
            break;
        default:
            actionSuccessText = "Error";
            break;
        // could add more status messages in here later
    };
    if (!actionSuccessText.equals("null")){
        response.setStatus(200);
        // If they refresh the page, the text should disappear, same for if they move to another page.
%>

<h3>The last action was <%=actionSuccessText%></h3>

<%
    }
%>

</body>
</html>
