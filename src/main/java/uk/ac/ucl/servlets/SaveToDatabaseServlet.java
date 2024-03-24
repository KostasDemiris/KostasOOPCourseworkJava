package uk.ac.ucl.servlets;

import uk.ac.ucl.model.DataWriter;
import uk.ac.ucl.model.JSONWriter;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;

@WebServlet("/SaveToDatabase.jsp")
public class SaveToDatabaseServlet extends HttpServlet{

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Model model = ModelFactory.getModel();
        String fileName = request.getParameter("fileNameInput");
        if (fileName.endsWith(".json")){
            try {
                JSONWriter.writeToJsonFile(fileName, model.getFrame());
                response.setStatus(100);
            }
            catch (IOException e){
                response.setStatus(-100);
            }

        } else {
            if (!fileName.endsWith(".csv")){
                fileName = fileName + ".csv";
            }
            DataWriter.writeCSVFile(fileName, model.getFrame());
        }
        response.sendRedirect("/patientList.html");

    }
}
