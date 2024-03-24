package uk.ac.ucl.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class patient
    // I was going to do this with a record, but they have a static number of fields, and using a class allowed me to
    // have more control over the inner workings of the class.
{
    private final HashMap<String, String> columns =  new LinkedHashMap<>();

    public patient(ArrayList<String> columnNames, ArrayList<String> values) throws IllegalArgumentException
    {
        if (columnNames.size()!=values.size()){
            throw new IllegalArgumentException();
        }
        for (int index = 0; index < columnNames.size(); index++){
            this.columns.put(columnNames.get(index), values.get(index));
        }
    }

    // I'm using this because I have to remove the "columns" tag to get a well formatted file for my reader...
    @JsonAnyGetter
    public HashMap<String, String> getColumns(){
        return new LinkedHashMap<>(this.columns);
        // This creates a new copy of columns to be read, without exposing the reference of columns, which allows editing
    }

    @JsonAnySetter
    public void set(String columnName, String value){
        this.columns.put(columnName, value);
    }

}

record patientCollection(ArrayList<patient> patients) { }
// Just for the formatting

public class JSONWriter {
    public static void writeToJsonFile(String fileName, DataFrame containerFrame) throws IOException{
        // I want the IOException to be handled by the caller, not this method
        patientCollection patients = new patientCollection(new ArrayList<>());
        ArrayList<String> columnNames = containerFrame.getColumnNames();
        System.out.println(columnNames);
        for (ArrayList<String> row: containerFrame.getAllRows())
        {
            patients.patients().add(new patient(columnNames, row));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(fileName), patients);
    }

}
