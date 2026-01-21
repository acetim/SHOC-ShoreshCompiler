public class AstBinOpExpression extends AstExpression{ //BINARY ONLY EXPRESSION SYSTEM!!
    private AstBinOpExpression left;
    private AstBinOpExpression right;


    public AstBinOpExpression() {
        super("");
        this.right = null;
        this.left = null;
    }

    public AstBinOpExpression(String value, AstBinOpExpression left, AstBinOpExpression right) {
        super(value);
        this.left = left;
        this.right = right;
    }

    public AstBinOpExpression getRight() {
        return right;
    }

    public void setRight(AstBinOpExpression right) {
        this.right = right;
    }


    public AstBinOpExpression getLeft() {
        return left;
    }

    public void setLeft(AstBinOpExpression left) {
        this.left = left;
    }
}
