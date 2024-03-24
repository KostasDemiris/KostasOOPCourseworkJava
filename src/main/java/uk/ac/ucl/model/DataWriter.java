package uk.ac.ucl.model;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataWriter {
public static void writeCSVFile(String fileName, DataFrame frame) throws IOException {
    if (!fileName.endsWith(".csv")) {fileName += ".csv";}

    CSVFormat csvFormat = CSVFormat.DEFAULT.withQuote(null);
    try (FileWriter fileWriter = new FileWriter(fileName); CSVPrinter printer = new CSVPrinter(fileWriter, csvFormat)){
        printer.printRecord(String.join(",", frame.getColumnNames()));
        for (ArrayList<String> row: frame.getAllRows(0)){
            try {
                printer.printRecord(String.join(",", row));
            }
            catch (IOException ioException){
                fileWriter.flush();
                fileWriter.close();
                printer.close();
                ioException.printStackTrace();
                throw ioException;
            }
        }
    }
    // no catch here since it'll be dealt with by whatever code is outside

}
}
