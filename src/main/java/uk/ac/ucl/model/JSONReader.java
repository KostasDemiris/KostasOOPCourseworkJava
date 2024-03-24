package uk.ac.ucl.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONReader {
    public JSONReader(){

    }
    public static DataFrame readFromJsonFile(String filename){
        DataFrame newDataFrame = new DataFrame();
        List<List<String>> StoredData = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode rootNode = objectMapper.readTree(new File(filename));
            JsonNode patientsNode = rootNode.get("patients");
            if (!patientsNode.isEmpty()) {
                // If it is empty, there'll be no column names or rows, so we can just skip all of this
                JsonNode firstPatient = patientsNode.get(0);

                Iterator<String> fieldNames = firstPatient.fieldNames();

                while (fieldNames.hasNext()) {
                    columnNames.add(fieldNames.next());
                }
                System.out.println("column names are: " + columnNames);
                for (JsonNode patient : patientsNode) {
                    List<String> patientRecord = new ArrayList<>();
                    for (String columnName: columnNames){
                        patientRecord.add(String.valueOf(patient.get(columnName)));
                        // In case they're not in the correct order (which happens in my writer implementation)
                    }
                    StoredData.add(patientRecord);
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }

        for (String columnName: columnNames) {newDataFrame.addColumn(new Column(columnName.toUpperCase()));}
        for (List<String> row: StoredData) {
            row.replaceAll(a -> a.replace("\"", ""));
            newDataFrame.addRow(row);
        }
        return newDataFrame;

    }
    public static ArrayList<String> getColumnNames(String filename){
        ArrayList<String> columnNames = new ArrayList<>();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(filename));
            JsonNode patientsNode = rootNode.get("patients");
            if (!patientsNode.isEmpty()){
                JsonNode firstPatient = patientsNode.get(0);
                Iterator<String> fieldNames = firstPatient.fieldNames();
                while (fieldNames.hasNext()) {
                    columnNames.add(fieldNames.next());
                }
            }
        }
        catch (IOException ignored){
            // Just skip all of the above and return the empty column names
        }
        return columnNames;
    }
}

