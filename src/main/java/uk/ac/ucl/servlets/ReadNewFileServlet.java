package uk.ac.ucl.servlets;

import uk.ac.ucl.Exceptions.NotFoundException;
import uk.ac.ucl.model.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;


@WebServlet("/ReadNewFile.jsp")
public class ReadNewFileServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Model model = ModelFactory.getModel();
//        for (Iterator<String> it = request.getParameterNames().asIterator(); it.hasNext(); ) {
//            String string = it.next();
//            System.out.println(string);
//        }
        boolean merge = false;
        try {
            merge = request.getParameter("merge").equals("true");
        }  catch (NullPointerException ignored){

        }
        String fileName = request.getParameter("fileNameInput");
        try {
            if (fileName.endsWith(".json")) {
                if (!merge) {
                    model.readFromJSONFile(fileName);
                } else {
                    model.addFileData(fileName, true);
                }

            } else {
                if (!fileName.endsWith(".csv")) {
                    fileName = fileName + ".csv";
                }
                if (merge) {
                    model.addFileData(fileName);
                } else {
                    model.readFile(fileName);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/patientList.html");
    }
}
