package Parsing.AstNodes;

import Tokenization.token;

import java.util.ArrayList;

public class AstFunctionExpression extends AstExpression{
    private ArrayList<AstExpression> arguments;

    public AstFunctionExpression(token value, ArrayList<AstExpression> arguments) {
        super(value);
        this.arguments = arguments;

    }

    public AstFunctionExpression(token value, AstExpression left, AstExpression right, ArrayList<AstExpression> arguments) {
        super(value, left, right);
        this.arguments = arguments;

    }

    public ArrayList<AstExpression> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        String s ="";
        s+=this.getToken().getValue()+"(";
        for (AstExpression arg :this.arguments){
            s+=PARSER_TESTING.getPrefixString(arg);
            s+=";";
        }
        s+=")";
        return s;
    }
}
