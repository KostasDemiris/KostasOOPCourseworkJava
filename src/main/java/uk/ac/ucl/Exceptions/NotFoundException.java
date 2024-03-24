package uk.ac.ucl.Exceptions;

public class NotFoundException extends Exception {
    public NotFoundException(){}
    public NotFoundException(String message){
        super(message);
    }
}
