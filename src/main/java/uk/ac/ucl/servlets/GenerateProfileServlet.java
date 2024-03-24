package uk.ac.ucl.servlets;

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
import java.util.Optional;

@WebServlet("/patientProfile.html")
public class GenerateProfileServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Model model = ModelFactory.getModel();
        String patientDetails = request.getParameter("patientID");
        Optional<String> getPatientID = Arrays.stream(patientDetails.split(" ")).findFirst();
        // The patient details returned are seperated bcy spaces. We could directly split and return it, but we can't
        // guarantee the split will be right for data past ID, the data could contain any number of special chars, incl.
        // spaces, so we just re-get the data and generate the profile with that 'clean' version.

        String patientID = " ";
        if (getPatientID.isPresent()){
            patientID = getPatientID.get();
        }

        List<String> patientData = model.getPatientRecord(patientID);
        List<String> columnNames = model.getColumnNames();
        // If there's no found record, it'll return an empty dummy string by the way.
        request.setAttribute("patientDetails", patientData);
        request.setAttribute("columnNames", columnNames);

        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/generatePatientProfile.jsp");
        dispatch.forward(request, response);

    }

}
