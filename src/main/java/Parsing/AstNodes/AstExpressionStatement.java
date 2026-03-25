package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstExpressionStatement extends AstStatement {
    private final String identifier;
    private final AstExpression expression;

    public AstExpressionStatement(String identifier, AstExpression expression) {
        super(TokenList.IDENTIFIER);
        this.identifier = identifier;
        this.expression = expression;
    }

    public String getIdentifier() {
        return identifier;
    }

    public AstExpression getExpression() {
        return expression;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstExpressionStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+identifier+"=");
        expression.print(indent);
        System.out.println();
    }
}
