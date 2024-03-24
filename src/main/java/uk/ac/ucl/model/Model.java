package uk.ac.ucl.model;

import java.io.IOError;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import uk.ac.ucl.Exceptions.DatabaseInconsistencyException;
import uk.ac.ucl.Exceptions.NotFoundException;

import javax.swing.*;
import javax.xml.crypto.Data;

public class Model
{
  // The example code in this class should be replaced by your Model class code.
  // The data should be stored in a suitable data structure.
  private DataFrame dataBase = new DataFrame();
  // This is the current database being used to store the data.
  // It can either be replaced or merged into when reading a new CSV file

  public DataFrame getFrame(){
    return this.dataBase.getClone();
    // This lets them get their own copy of the frame being stored, without being able to modify the stored copy.
  }
  public Boolean storePatientData(String filePath){
    // Attempts to store Patient Data from a read csv file into the database, returning true if successful else false.
    try {
      this.dataBase = DataLoader.createFrameFromFile(filePath, this.dataBase);
      // This overwrites the current data in the database
      return true;
    }
    catch (IOException e){
      return false;
    }
  }
  public ArrayList<ArrayList<String>> getStoredData(){
    // This returns every row stored (as an arrayList of Strings) in arrayList format.
    return this.dataBase.getAllRows();
  }
  public ArrayList<ArrayList<String>> returnFileData(String filePath){
    // This returns the data contained in a csv file without committing to storing it, in situations like previewing
    // or merging
    DataFrame frame = DataLoader.createFrameFromFile(filePath);
    return frame.getAllRows();
  }
  public ArrayList<String> getPatientRecord(String ID){
    try {
      return this.dataBase.getEntry("ID", ID);
    }
    catch (NotFoundException e){
      String[] dummyArray = new String[this.dataBase.getColumnCount()];
      Arrays.fill(dummyArray, "");
      return new ArrayList<>(Arrays.asList(dummyArray));
      // So if they search for an array with that ID, and it doesn't exist, it will return an empty record
    }
  }
  public Boolean addFileData(String filePath){
    return addFileData(filePath, false);
  }

  public Boolean addFileData(String filePath, boolean JSON){
    // add the data from one file to the current database, under some restrictions (the columns must be the same)
    try {
      List<String> columnNames;
      if (!JSON) {
        columnNames = DataLoader.getColumnNames(filePath);
      }
      else{
        columnNames = JSONReader.getColumnNames(filePath);
      }
      List<String> dataBaseColumnNames = this.dataBase.getColumnNames();
      ArrayList<Integer> columnPositions = new ArrayList<>();
      // This stores where each column from the current database is stored in the new array

      for (String column: columnNames){
        // This ensures that the data has compatible column names, and if it's in a different order with similar column
        // names, ensures that the columns are read from in the correct order to maintain compatability
        boolean hasBeenFound = false;

        for (int index = 0; index < dataBaseColumnNames.size() && !hasBeenFound; index++){
          int finalIndex = index;
          if (column.equals(dataBaseColumnNames.get(index)) && columnPositions.stream().noneMatch
                  ((col) -> col == finalIndex)){
            // This is done in the case that there are repeat column names with
            // different data, to not store the first twice and ignore the second
            columnPositions.add(index);
            hasBeenFound = true;
          }
        }
        if (!hasBeenFound) {return false;}
        // If it isn't compatible, don't bother trying to actually read it
      }
      DataFrame other;
      if (!JSON) {
        other = DataLoader.createFrameFromFile(filePath);
      }
      else{
        other = JSONReader.readFromJsonFile(filePath);
      }
      ArrayList<String> IDs = this.getColumnData("ID");
      for (ArrayList<String> row: other.getAllRows(0)){
        try {
          if (!IDs.contains(row.get(columnPositions.getFirst()))) {
            // We don't want duplicate ID's, if it doesn't have one, don't add it
            ArrayList<String> rearrangedColumn = new ArrayList<>();
            for (int index : columnPositions) {
              rearrangedColumn.add(row.get(index));
            }
            this.dataBase.addRow(rearrangedColumn);
          }
        }
        catch (IllegalArgumentException exception){
          // Doesn't add that row, and no cleanup needed since it throws an error pre-storing operation.
        }
      }

    }
    catch (IOException e){
      return false;
    }
    return true;
  }
  public ArrayList<String> getColumnData(String column){
    try{
      return this.dataBase.getColumnData(column);
    }
    catch (NotFoundException notFoundException){
      System.out.println("Did not find column " + column + " in the database");
      System.out.println("The columns are: " + this.dataBase.getColumnNames());
      return new ArrayList<String>();
    }
  }
  public ArrayList<String> getColumnNames(){
    return this.dataBase.getColumnNames();
  }


  // This method illustrates how to read csv data from a file.
  // The data files are stored in the root directory of the project (the directory your project is in),
  // in the directory named data.
  public void readFile(String fileName){
    try {
      this.dataBase = DataLoader.createFrameFromFile(fileName);
    }
    catch (IOError e){
      // This means that the provided file had an issue
      e.printStackTrace();
      this.dataBase = new DataFrame();
    }
  }

  // This also returns dummy data. The real version should use the keyword parameter to search
  // the data and return a list of matching items.
  public List<String> searchFor(String keyword)
  {
    return this.searchFor(keyword, false);
  }
  public List<String> searchFor(String keyword, boolean absolute){
    ArrayList<ArrayList<String>> relevantRows;
    if (!absolute) {
      // Like searchFor, except this returns any row that has a word that CONTAINS the keyword
      relevantRows = this.dataBase.getAnythingContaining(keyword);
    }
    else{
      // This one will return a list of all entries that have a parameter that matches the keyword
      relevantRows = this.dataBase.getAnyMatch(keyword);
    }
    return new ArrayList<>(this.convertRowsToString(relevantRows));
  }


  public List<String> getPatientNames() {
    ArrayList<String> fullNames = new ArrayList<>();
    ArrayList<String> suffixes = this.getColumnData("SUFFIX");
    ArrayList<String> firstNames = this.getColumnData("FIRST");
    ArrayList<String> lastNames = this.getColumnData("LAST");

    for (int index = 0; index < this.dataBase.getRowCount(); index++){
      try{
        if ("".equals(suffixes.get(index))){
          fullNames.add(firstNames.get(index) + " " + lastNames.get(index));
        }
        else{
          fullNames.add(suffixes.get(index) + " " + firstNames.get(index) + " " + lastNames.get(index));
        }
      }
      catch (ArrayIndexOutOfBoundsException exception){
        // This means for some reason that some column is longer than the others, so we just return all the full Columns
        return fullNames;
      }
    }
    return fullNames;
  }
  public ArrayList<String> convertRowsToString(ArrayList<ArrayList<String>> rows){
    ArrayList<String> statements = new ArrayList<>();
    for (ArrayList<String> row: rows){
      statements.add(String.join(", ", row));
    }
    return statements;
  }
  public ArrayList<int[]> getAgeData(){
    ArrayList<String> ages = this.getColumnData("BIRTHDATE");
    ArrayList<int[]> separateAges = new ArrayList<>();
    for (String age: ages) {
      separateAges.add(Arrays.stream(age.split("-")).mapToInt(Integer:: parseInt).toArray());
    }
    // Now we have all the ages in a format where they are, birth year, birth month, birthday, represented as integers
    return separateAges;
  }
  public ArrayList<String> getOldestPatient(){
    ArrayList<int[]> ages = this.getAgeData();
    int[] oldestBirthDate = ages.getFirst();
    ArrayList<String> relevantPatient = this.dataBase.getRow(0);
    for (int index = 1; index < ages.size(); index++){
      if (ages.get(index)[0] < oldestBirthDate[0]){
        oldestBirthDate = ages.get(index);
        relevantPatient = this.dataBase.getRow(index);
      }
      else if (ages.get(index)[0] == oldestBirthDate[0]) {
        if (ages.get(index)[1] < oldestBirthDate[1]) {
          oldestBirthDate = ages.get(index);
          relevantPatient = this.dataBase.getRow(index);
        }
        else if (ages.get(index)[1] == oldestBirthDate[1]) {
          if (ages.get(index)[2] < oldestBirthDate[2]) {
            oldestBirthDate = ages.get(index);
            relevantPatient = this.dataBase.getRow(index);
          }
        }
      }
    }
    return relevantPatient;
  }
  public void editRow(List<String> newColumn) throws NotFoundException {
    this.dataBase.editRow(newColumn.getFirst(), newColumn);
  }
  public boolean deleteRow(String ID){
    // If it returns true, it
    try {
      // This returns whether the row was deleted, if this returns false, it means the ID couldn't be found
        return this.dataBase.deleteRow(ID);
    }
    catch (DatabaseInconsistencyException exception){
      // This means we have to reload the database because there's been an issue encountered with the database
      // (structurally and in a way that cannot be fixed e.g. in this case differing numbers of columns)
      this.dataBase = DataLoader.createFrameFromFile(this.dataBase.sourceFile);
      return false;
    }
  }
  public boolean addRow(List<String> newRow){
    try{
      this.dataBase.addRow(newRow);
      return true;
    }
    catch (IllegalArgumentException exception){
      System.out.println("illegal...");
      return false;
    }
  }
  public HashMap<String, ArrayList<String>> getHouseholds(){
    // Basically, it returns each unique address and the name + ID of each person that lives in it
    HashMap<String, ArrayList<String>> households = new HashMap<>();
    for (int index = 0; index < this.dataBase.getRowCount(); index++){
      String address = this.dataBase.getValue("ADDRESS", index);
      if (households.containsKey(address)){
        String name = this.dataBase.getValue("PREFIX", index) + " " + this.dataBase.getValue(
                "FIRST", index) + " " + this.dataBase.getValue("LAST", index);
        households.get(address).add(this.dataBase.getRow(index).getFirst() + ": " + name);
        // Since it's a reference that's contained in the hashmap, we can update the values in the object it refers to
        // and not have to change the reference
      }
      else{
        String name = this.dataBase.getValue("PREFIX", index) + " " + this.dataBase.getValue(
                "FIRST", index) + " " + this.dataBase.getValue("LAST", index);
        households.put(address, new ArrayList<String>(Collections.singletonList(this.dataBase.getValue("ID", index) + ": " + name)));
        // This just create a new household with a single ID
      }
    }
    return households;
  }
  public ArrayList<String> getUnique(String columnName){
    return this.getColumnData(columnName).stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    // This just gets all the column data, gets the unique elements and then adds them into a new arraylist
  }
  public ArrayList<String> getYoungestPatient(){
    ArrayList<int[]> ages = this.getAgeData();
    int[] youngestBirthDate = ages.getFirst();
    ArrayList<String> relevantPatient = this.dataBase.getRow(0);
    for (int index = 1; index < ages.size(); index++){
      if (ages.get(index)[0] > youngestBirthDate[0]){
        youngestBirthDate = ages.get(index);
        relevantPatient = this.dataBase.getRow(index);
      }
      else if (ages.get(index)[0] == youngestBirthDate[0]) {
        if (ages.get(index)[1] > youngestBirthDate[1]) {
          youngestBirthDate = ages.get(index);
          relevantPatient = this.dataBase.getRow(index);
        }
        else if (ages.get(index)[1] == youngestBirthDate[1]) {
          if (ages.get(index)[2] > youngestBirthDate[2]) {
            youngestBirthDate = ages.get(index);
            relevantPatient = this.dataBase.getRow(index);
          }
        }
      }
    }
    return relevantPatient;
  }

  public boolean writeToCSVFile(DataFrame dataFrame, String pathName){
    if (dataFrame.sourceFile.equals(pathName)){
      throw new IllegalArgumentException("Cannot save the database to the file that was read from.");
    }
    try{
      DataWriter.writeCSVFile(pathName, dataFrame);
      return false;
    }
    catch (IOException exception){
      return false;
    }
  }
  public void readFromJSONFile(String pathName) throws IOException {
    DataFrame readFrame = JSONReader.readFromJsonFile(pathName);
    if (!readFrame.isEmpty()){
      this.dataBase = readFrame;
    }
    else{
      throw new IOException();
    }
    // If it fails to read it, it'll just create an empty frame
  }
  public void replaceDatabase(DataFrame frame){
    this.dataBase = frame;
  }
}
