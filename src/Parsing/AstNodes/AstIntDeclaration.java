package Parsing.AstNodes;

public class AstIntDeclaration extends AstVarDeclaration {
    private AstExpression Expression;

    public AstIntDeclaration(String identifier, int sizeInBytes, AstExpression expression) {
        super(identifier, sizeInBytes);
        Expression = expression;//can be null if not initialized
    }

    public AstExpression getExpression() {
        return Expression;
    }

    public void setExpression(AstExpression expression) {
        Expression = expression;
    }
}
