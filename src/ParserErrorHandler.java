import java.util.ArrayList;

public class ParserErrorHandler {
    private ArrayList<String> errors;

    public ParserErrorHandler() {
        this.errors = new ArrayList<>();

    }

    public void reportError(String message) throws ParserException{
        errors.add(message);
        throw new ParserException(message);
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
