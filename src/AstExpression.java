public class AstExpression extends AstElement{
    private String value;

    public AstExpression(String value) {
        Value = value;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
