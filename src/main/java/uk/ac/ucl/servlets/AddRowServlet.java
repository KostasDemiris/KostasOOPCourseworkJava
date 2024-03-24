package uk.ac.ucl.servlets;

import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/AddRow.html")
public class AddRowServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Model model = ModelFactory.getModel();
        List<String> rowData = Arrays.asList(request.getParameter("rowData").split(","));
        for (int index = 0; index < rowData.size(); index++){
            if (rowData.get(index).chars().allMatch(x -> x == ' ')){
                rowData.set(index, "");
            }
            // so any all-spaces string is replaced with an empty one, for easier identifying
        }
        if (model.addRow(rowData)){
            response.setStatus(-100);
        }
        else{
            response.setStatus(100);
        }

        response.sendRedirect("/patientList.html");
    }
}
