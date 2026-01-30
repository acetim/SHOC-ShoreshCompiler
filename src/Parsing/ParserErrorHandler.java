package Parsing;

import Exceptions.ParserException;

import java.util.ArrayList;

public class ParserErrorHandler {
    private ArrayList<String> errors;
    private boolean ErrorsPresent;

    public ParserErrorHandler() {
        this.errors = new ArrayList<>();
        this.ErrorsPresent=false;
    }

    public void reportError(String message) throws ParserException{
        errors.add(message);
        this.ErrorsPresent=true;
        throw new ParserException(message);
    }
    public boolean errorArePresent(){
        return this.ErrorsPresent;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
