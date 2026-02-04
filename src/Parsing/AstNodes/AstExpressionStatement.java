package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstExpressionStatement extends AstStatement {
    private String identifier;
    private AstExpression expression;

    public AstExpressionStatement(String identifier, AstExpression expression) {
        super(TokenList.IDENTIFIER);
        this.identifier = identifier;
        this.expression = expression;
    }
}
