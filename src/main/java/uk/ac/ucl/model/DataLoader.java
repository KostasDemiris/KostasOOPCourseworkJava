package uk.ac.ucl.model;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;
import java.util.List;

public class DataLoader {
    public static DataFrame createFrameFromFile(String pathToCSV, DataFrame container)
            throws IOException {
        container.setSourceFile(pathToCSV);
        File file = new File(pathToCSV);
        if (!file.exists()) {throw new FileNotFoundException("No file found");}

        // Container must be empty or else this won't work.
        try (Reader reader = new FileReader(pathToCSV);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())){
            List<CSVRecord> records = csvParser.getRecords();
            List<String> headers = csvParser.getHeaderNames();

            for (String column_name: headers) {
                container.addColumn(new Column(column_name));
            }
            for (int index = 0; index < records.size(); index++) {
                ArrayList<String> cur_row = new ArrayList<>();
                for (int col = 0; col < headers.size(); col++) {
                    try {
                        cur_row.add(records.get(index).get(col));
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        cur_row.add("");
                        System.out.println(e + " Out of bound exception");
                        // If there's no data filled in for a column in the database, add in empty fields to signify that
                    }
                }
                try {
                    container.addRow(cur_row);
                }
                catch(IllegalArgumentException e){
                        e.printStackTrace();
                        // Just report but ignore it, it means that there is an issue with the number of columns
                    // of the input
                    }

            }
        }
        catch (IOException e){
            e.printStackTrace();
            throw e;
            // Add something more here potentially later?
        }
        return container;
    }
    public static DataFrame createFrameFromFile(String pathToCSV){
        try {
            return createFrameFromFile(pathToCSV, new DataFrame());
        }
        catch (IOException exception){
            // Come back and change this perhaps?
            System.out.println("File path not valid");
        }
        return new DataFrame();
    }
    public static List<String> getColumnNames(String filePath) throws IOException{
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())){
            return csvParser.getHeaderNames();
        }
        catch (IOException e){
            e.printStackTrace();
            throw e;
        }
    }
}
