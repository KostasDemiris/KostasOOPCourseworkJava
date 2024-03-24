package uk.ac.ucl.servlets;

import uk.ac.ucl.Exceptions.NotFoundException;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/TakeRowInput.html")
public class TakeRowInputServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Model model = ModelFactory.getModel();
        request.setAttribute("columnNames", model.getColumnNames());
        request.setAttribute("actionName", request.getParameter("actionName"));

        if (request.getParameter("actionName").equals("editRow")){
            request.setAttribute("rowData", model.getPatientRecord(request.getParameter("patientID")));
        }
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/TakeRowInput.jsp");
        dispatch.forward(request, response);

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Model model = ModelFactory.getModel();
        ArrayList<String> editedValues = new ArrayList<>();

        for (String columnName : model.getColumnNames()) {
            if (request.getParameter("input" + columnName).isEmpty()){
                editedValues.add(" ");
                // Completely empty strings cause issues when splitting and joining because they are ignored
                // This makes it difficult to pass data between
            }
            else{
                editedValues.add(request.getParameter("input" + columnName));
            }
        }
        String rowData = String.join(",", editedValues) + ",";
        // This means if there is an empty zip code, it won't overwrite it


        switch (request.getParameter("actionName")){
            case ("addRow"):{
                response.sendRedirect("/AddRow.html?rowData="+rowData);
                break;
            }
            case ("editRow"):{
                response.sendRedirect("/UpdateDatabase.html?rowData="+rowData);;
                break;
            }
            case ("deleteRow"):{
                response.sendRedirect("/DeleteRow.html");
                break;
            }
            default:{
                response.sendRedirect("index.html");
                break;
            }
        }
    }
}
