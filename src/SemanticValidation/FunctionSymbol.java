package SemanticValidation;

import java.util.ArrayList;
import Parsing.AstNodes.AstParameter;
import Tokenization.TokenList;

public class FunctionSymbol {
    private ArrayList<AstParameter> parameters;
    private TokenList returnType;

    public FunctionSymbol( TokenList returnType) {
        this.parameters = new ArrayList<>();
        this.returnType = returnType;
    }
    public void addParam(AstParameter a){
        this.parameters.add(a);
    }

    public ArrayList<AstParameter> getParameters() {
        return parameters;
    }

    public TokenList getReturnType() {
        return returnType;
    }
}
