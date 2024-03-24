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
import java.util.List;

@WebServlet("/UpdateDatabase.html")
public class UpdateDatabaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        Model model = ModelFactory.getModel();
        List<String> rowData = Arrays.asList(request.getParameter("rowData").split(","));
        try {
            model.editRow(rowData);
            response.setStatus(100);
        }
        catch (NotFoundException e){
            response.setStatus(-100);
        }

        // Because I want it to go back to whatever page called the edit after the edit has occurred or whatever
        // that page wants to redirect to after an edit.

        response.sendRedirect("/patientProfile.html?patientID="+rowData.getFirst());

    }
}
