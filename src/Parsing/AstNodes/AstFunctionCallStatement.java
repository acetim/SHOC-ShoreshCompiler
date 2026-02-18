package Parsing.AstNodes;

import SemanticValidation.Visitor;
import Tokenization.TokenList;


import java.util.ArrayList;
/*
USED FOR VOID FUNCTION CALLS
 */
public class AstFunctionCallStatement extends AstStatement{
    private final String funcName;
    private final ArrayList<AstExpression> args;

    public AstFunctionCallStatement(String funcName, ArrayList<AstExpression> args) {
        super(TokenList.FUNCTION_CALL);
        this.funcName = funcName;
        this.args = args;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstFunctionCallStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"call " +funcName+" args=");
        for(AstExpression exp :args){
            exp.print(indent);
        }
        System.out.println();
    }
}
