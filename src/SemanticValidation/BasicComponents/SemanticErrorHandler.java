package SemanticValidation.BasicComponents;

import java.util.ArrayList;

public class SemanticErrorHandler {
    private ArrayList<String> errors;

    public SemanticErrorHandler() {
        this.errors = new ArrayList<>();
    }
    public void add(String err){
        this.errors.add(err);
    }
    public void printAllErrors(){
        for(String err:this.errors){
            System.err.println(err);
        }
    }
    public boolean errorsPresent(){
        return !errors.isEmpty();
    }
}
