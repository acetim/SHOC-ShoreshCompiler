public class AstExpression extends AstElement{ //BINARY ONLY EXPRESSION SYSTEM!!
    private String Value;
    private AstExpression left;
    private AstExpression right;


    public AstExpression() {
        Value = "";
        this.right = null;
        this.left = null;
    }


    public AstExpression getRight() {
        return right;
    }

    public void setRight(AstExpression right) {
        this.right = right;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public AstExpression getLeft() {
        return left;
    }

    public void setLeft(AstExpression left) {
        this.left = left;
    }
}
