package uk.ac.ucl.model;

import uk.ac.ucl.Exceptions.DatabaseInconsistencyException;
import uk.ac.ucl.Exceptions.NotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DataFrame implements Cloneable {
    Map<String, Column> Columns = new HashMap<>();
    ArrayList<String> columnNames = new ArrayList<>();
    String sourceFile;
    public void addColumn(Column new_Column) {
        this.Columns.put(new_Column.get_name(), new_Column);
        columnNames.add(new_Column.get_name());
    }
    public void setSourceFile(String sourceFile){
        this.sourceFile = sourceFile;
    }
    public ArrayList<String> getColumnNames(){
        return this.columnNames;
    }
    public int getColumnCount() {return this.Columns.size();}
    public int getRowCount() {
        if (!this.Columns.isEmpty()) {
//            Map.Entry<String, Column> first_entry = this.Columns.entrySet().iterator().next();
//            return first_entry.getValue().get_size();
            return this.Columns.get(this.columnNames.getFirst()).get_size();
        }
        return 0;
    }
    public String getValue(String columnName, int row)
            throws ArrayIndexOutOfBoundsException{
        if (this.Columns.containsKey(columnName)){
            try {
                return this.Columns.get(columnName).get_row_value(row);
            }
            catch (ArrayIndexOutOfBoundsException e){
                return "Out Of Bounds";
            }
        }
        return "No Column With That Name";
    }
    public ArrayList<String> getEntry(String columnName, String value) throws NotFoundException{
        // This will return the first row that satisfies the conditions, so for the lookup of a specific entry,
        // Use a uniquely identifying column
        if (this.Columns.containsKey(columnName)){
            try {
                return this.getRow(this.Columns.get(columnName).getRow(value));
            }
            catch (NotFoundException e){
                throw new NotFoundException("Entry");
                // The entry value provided couldn't be found
            }
        }
        throw new NotFoundException("Column");
        // The column being searched didn't exist
    }
    public ArrayList<String> getColumnData(String columnName) throws NotFoundException {
        try{
            return this.Columns.get(columnName).get_all_values();
        }
        catch (NullPointerException exception){
            throw new NotFoundException("Column");
        }
    }
    public ArrayList<ArrayList<String>> getAnyMatch(String parameter){
        // Basically, if the parameter is equal to any column's value, return that entire row
        HashMap<Integer, Boolean> relevantRows = new HashMap<>(this.getRowCount());
        // Having it as a hashmap allows us to check whether the row is already in instantly
        for (Map.Entry<String, Column> column: this.Columns.entrySet()){
            try{
                for (Integer row: column.getValue().getAllRows(parameter)){
                    relevantRows.put(row, true);
                }
                // Storing the rows rather than their contents allows us to prevent repeats
            }
            catch (NotFoundException ignored){
                // No match, no add
            }
        }
        ArrayList<ArrayList<String>> returnStrings = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> row: relevantRows.entrySet()){
            returnStrings.add(this.getRow(row.getKey()));
        }
        return returnStrings;
    }
    public int getIndex(String columnName, String value) throws NotFoundException {
        Column searchColumn = this.Columns.get(columnName);
        return searchColumn.getRow(value);
        // This throws a notFoundException if there's no such row, but that's to be handled by the caller
    }
    public void editRow(String ID, List<String> updatedVer) throws NotFoundException, IllegalArgumentException{
        if (updatedVer.size() != this.getColumnCount()){
            throw new IllegalArgumentException("Invalid length of new row");
        }
        try {
            int index = this.getIndex("ID", ID);
            AtomicInteger column = new AtomicInteger();
            for (String columnName: this.columnNames){
                this.Columns.get(columnName).set_row_value(index, updatedVer.get(column.getAndIncrement()));
            }
        } catch (NotFoundException e) {
            // There was an invalid ID used as the basis to edit the row
            e.printStackTrace();
            throw new NotFoundException("This ID was not found in the database");
        }
    }
    public boolean deleteRow(String ID) throws DatabaseInconsistencyException{
        try {
            int index = this.getIndex("ID", ID);
            ArrayList<String> originalRow = this.getRow(index);
            for (Column column: this.Columns.values()){
                try{
                    if (!column.removeRow(index)){
                        // do rollback, inserting original values back into deleted columns and return false;
                        boolean alrDeleted = true;
                        List<Column> columnRefList = this.Columns.values().stream().toList();
                        for (int currCol = 0; currCol < this.getColumnCount() && alrDeleted; currCol++){
                            if (columnRefList.get(currCol).get_name().equals(column.get_name())){
                                alrDeleted = false;
                                // Until we encounter the column in which the value wasn't deleted, we will insert the
                                // old values in, when we get to the one that was kept, we stop to prevent duplicates
                            }
                            else{
                                columnRefList.get(index).insertAtRow(index, originalRow.get(index));
                                // This can throw Database inconsistency, but we'll let it pass because we want
                                // it to be handled by whichever caller functions from model called it.
                            }
                        }
                        return false;
                    }
                }
                catch(ArrayIndexOutOfBoundsException e){
                    throw new DatabaseInconsistencyException();
                    // Given that we've found the ID, if the rows are uneven, that means the database is incorrectly
                    // formed. This is passed out, but should be handled by reloading the database completely.
                }
            }
        }
        catch (NotFoundException e){
            // ID did not exist in the database
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<ArrayList<String>> getAnythingContaining(String parameter){
        HashMap<Integer, Boolean> relevantRows = new HashMap<>(this.getRowCount());
        for (Map.Entry<String, Column> column: this.Columns.entrySet()){
            try{
                for (Integer row: column.getValue().getAllRowsContaining(parameter)){
                    relevantRows.put(row, true);
                }
            }
            catch (NotFoundException ignored){

            }
        }
        ArrayList<ArrayList<String>> returnStrings = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> row: relevantRows.entrySet()){
            returnStrings.add(this.getRow(row.getKey()));
        }
        return returnStrings;
    }
    public void putValue(String columnName, int row, String value)
            throws NotFoundException, ArrayIndexOutOfBoundsException {
        if (this.Columns.containsKey(columnName)) {
            this.Columns.get(columnName).set_row_value(row, value);
            // This can return an OutOfBounds exception, which I won't catch here but should be dealt with by the
            // Caller for any assignment functions
        }
        throw new NotFoundException("Column Name Not Found");
    }
    public void addValue(String columnName, String value) throws NotFoundException{
        if (this.Columns.containsKey(columnName)){
            this.Columns.get(columnName).add_row_value(value);
        }
        throw  new NotFoundException("Column Name Not Found");
    }
    public boolean isEmpty(){
        return (this.Columns.isEmpty());
    }
    public void addRow(List<String> newRow) throws IllegalArgumentException {
        try {
            if (newRow.size() != this.getColumnCount() || this.getColumnData("ID").contains(newRow.getFirst()))
            {
                throw new IllegalArgumentException("Invalid new row");
                // Either too many rows or the provided ID is already taken.
            }
        } catch (NotFoundException e) {
            // ignore this, this only occurs for if there is no columns in that database, which is covered by the first
            // length clause
        }
        int index = 0;
        for (String columnName: this.columnNames){
            this.Columns.get(columnName).add_row_value(newRow.get(index));
            index++;
        }
    }
    public ArrayList<String> getRow(int index){
        ArrayList<String> row = new ArrayList<>();
        for (String columnNames: this.columnNames){
            row.add(this.Columns.get(columnNames).get_row_value(index));
        }
        return row;
    }
    public ArrayList<ArrayList<String>> getAllRows(int startingRow){
        ArrayList<ArrayList<String>> returnable = new ArrayList<>();
        for (int index = startingRow; index < this.getRowCount(); index++) {returnable.add(this.getRow(index));}
        return returnable;
    }
    public ArrayList<ArrayList<String>> getAllRows(){
        return getAllRows(0);
    }

    public DataFrame getClone(){
        DataFrame returnFrame = new DataFrame();
        for (String column: this.columnNames){
            try {
                returnFrame.addColumn(new Column(column, (ArrayList<String>) this.getColumnData(column).clone()));
            }
            catch (NotFoundException ignored){
                // Since we're reading column names, then checking if they're in column names, it should never throw
                // an exception
            }
        }
        return returnFrame;
    }
}
