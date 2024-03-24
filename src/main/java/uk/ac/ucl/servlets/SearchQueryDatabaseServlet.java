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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/queryDatabase.html")
public class SearchQueryDatabaseServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Use the model to do the search and put the results into the request object sent to the
        // Java Server Page used to display the results.
        Model model = ModelFactory.getModel();
        for (Iterator<String> it = request.getParameterNames().asIterator(); it.hasNext(); ) {
            String name = it.next();
        }
        if (!request.getParameterNames().asIterator().hasNext()){
            System.out.println("Empty");
        }
        String functionToCall = (String) request.getParameter("actionType");
        List<String> searchResult = new ArrayList<>();
        String queryDataType;
        switch (functionToCall) {
            case "getOldest": {
                searchResult = model.getOldestPatient();
                queryDataType = "singlePatient";
                break;
            }
            case "getYoungest": {
                searchResult = model.getYoungestPatient();
                queryDataType = "singlePatient";
                break;
            }
            case "getHouseholds": {
                HashMap<String, ArrayList<String>> households = model.getHouseholds();

                for (Map.Entry<String, ArrayList<String>> household: households.entrySet()){
                    searchResult.add(household.getKey() + "," + String.join(",", household.getValue()));
                }
                queryDataType = "HouseholdData";
                break;
            }
            default: {
                searchResult = model.convertRowsToString(model.getStoredData());
                queryDataType = "patientList";
                break;
            }
        }
        request.setAttribute("result", searchResult);
        request.setAttribute("queryDataType", queryDataType);
        request.setAttribute("columnNames", model.getColumnNames());

        // Invoke the JSP page.
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/queryResult.jsp");
        dispatch.forward(request, response);
    }
}

