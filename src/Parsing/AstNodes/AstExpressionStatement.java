package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstExpressionStatement extends AstStatement {
    private final String identifier;
    private final AstExpression expression;

    public AstExpressionStatement(String identifier, AstExpression expression) {
        super(TokenList.IDENTIFIER);
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+identifier+"=");
        expression.print(indent);
        System.out.println();
    }
}
