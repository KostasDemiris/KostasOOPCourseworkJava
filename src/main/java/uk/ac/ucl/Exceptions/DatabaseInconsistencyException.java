package uk.ac.ucl.Exceptions;

public class DatabaseInconsistencyException extends Exception{
    public DatabaseInconsistencyException(){}
    public DatabaseInconsistencyException(String message){
        super(message);
    }
}
