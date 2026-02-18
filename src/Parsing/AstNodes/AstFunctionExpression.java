package Parsing.AstNodes;

import SemanticValidation.Visitor;
import Tokenization.token;

import java.util.ArrayList;

public class AstFunctionExpression extends AstExpression{
    private final ArrayList<AstExpression> arguments;

    public AstFunctionExpression(token value, ArrayList<AstExpression> arguments) {
        super(value);
        this.arguments = arguments;

    }

    public ArrayList<AstExpression> getArguments() {
        return arguments;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstFunctionExpression(this);
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
