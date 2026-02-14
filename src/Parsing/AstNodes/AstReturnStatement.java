package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstReturnStatement extends AstStatement{
    private AstExpression returnExpression;

    public AstReturnStatement( AstExpression returnExpression) {
        super(TokenList.KEYWORD_RETURN);
        this.returnExpression = returnExpression;
    }

    public AstExpression getReturnExpression() {
        return returnExpression;
    }
}
