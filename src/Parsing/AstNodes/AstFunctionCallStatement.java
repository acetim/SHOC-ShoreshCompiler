package Parsing.AstNodes;

import Tokenization.TokenList;

import java.beans.Expression;
import java.util.ArrayList;
/*
USED FOR VOID FUNCTION CALLS
 */
public class AstFunctionCallStatement extends AstStatement{
    private String funcName;
    private ArrayList<AstExpression> args;

    public AstFunctionCallStatement(String funcName, ArrayList<AstExpression> args) {
        super(TokenList.FUNCTION_CALL);
        this.funcName = funcName;
        this.args = args;
    }
}
