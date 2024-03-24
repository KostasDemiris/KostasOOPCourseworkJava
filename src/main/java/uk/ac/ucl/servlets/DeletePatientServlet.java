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

@WebServlet("/DeleteRow.html")
public class DeletePatientServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Model model = ModelFactory.getModel();
        String patientID = request.getParameter("patientID");

        if (model.deleteRow(patientID)){
            response.setStatus(100);
        }
        else{
            response.setStatus(-100);
        }

        response.sendRedirect("patientList.html");

    }
}
