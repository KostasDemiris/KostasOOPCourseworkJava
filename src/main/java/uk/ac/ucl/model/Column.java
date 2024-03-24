package uk.ac.ucl.model;

import uk.ac.ucl.Exceptions.DatabaseInconsistencyException;
import uk.ac.ucl.Exceptions.NotFoundException;

import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;


public class Column{
    private String name;
    private ArrayList<String> data;

    public Column(String name, ArrayList<String> data) {
        this.name = name;
        this.data = data;
    }
    public Column(String name){
        this.name = name;
        this.data = new ArrayList<>();
    }
    public int get_size() {return this.data.size();}
    public String get_name() {return this.name;}
    public String get_row_value(int index) throws ArrayIndexOutOfBoundsException {
        return this.data.get(index);
        // If the index < 0 or >= len(data), it'll be out of bounds. We could deal with this locally by passing if
        // Those conditions are satisfied, but then we won't be able to detect the error from the caller function
    }
    public void set_row_value(int index, String value) throws ArrayIndexOutOfBoundsException {
        this.data.set(index, value);
    }
    public void add_row_value(String value) {this.data.add(value);}
    public ArrayList<String> get_all_values() {return this.data;}
    public int getRow(String searchVal) throws NotFoundException {
        for (int index = 0; index < this.data.size(); index++){
            if (this.data.get(index).equals(searchVal)){
                return index;
            }
        }
        throw new NotFoundException();
    }
    public ArrayList<Integer> getAllRowsContaining(String searchVal) throws NotFoundException {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int index = 0; index < this.data.size(); index++){
            if (this.data.get(index).toLowerCase().contains(searchVal.toLowerCase())) {rows.add(index);}
        }
        if (rows.isEmpty()) {throw new NotFoundException();}
        return rows;
    }
    public boolean removeRow(int row) throws  ArrayIndexOutOfBoundsException{
        if (row > this.get_size()){
            throw new ArrayIndexOutOfBoundsException();
        }
        return (this.data.remove(row) != null);
    }
    public void insertAtRow(int row, String value) throws DatabaseInconsistencyException {
        try {
            this.data.add(row, value);
        }
        catch(ArrayIndexOutOfBoundsException e){
            throw new DatabaseInconsistencyException();
        }
    }
    public ArrayList<Integer> getAllRows(String searchVal) throws NotFoundException{
        ArrayList<Integer> rows = new ArrayList<>();
        for (int index = 0; index < this.data.size(); index++){
            if (this.data.get(index).equals(searchVal)){
                rows.add(index);
            }
        }
        if (rows.isEmpty()){
            throw new NotFoundException();
        }
        return rows;
    }
    public Boolean mergeColumns(Column column){
        if (!this.get_name().equals(column.get_name())){
            return false;
        }
        this.data.addAll(column.get_all_values());
        return true;
    }
}