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

    @Override
    public void print(String indent) {
        System.out.print(indent+"INT "+this.getVarName());
        if(this.Expression!=null) {
            System.out.print("=");
            this.Expression.print(indent);

        }
        System.out.println();
    }
}
