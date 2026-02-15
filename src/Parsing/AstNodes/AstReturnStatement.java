package Parsing.AstNodes;

import Tokenization.TokenList;
/*
IF RETURN HAS NO VALUE(VOID FUNC RETURN)
returnExpression will have no value!!(will be null)
 */
public class AstReturnStatement extends AstStatement{
    private AstExpression returnExpression;

    public AstReturnStatement( AstExpression returnExpression) {
        super(TokenList.KEYWORD_RETURN);
        this.returnExpression = returnExpression;
    }

    public AstExpression getReturnExpression() {
        return returnExpression;
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"return ");
        if(this.returnExpression!=null) {
            this.returnExpression.print(indent);
        }
        System.out.println();
    }
}
