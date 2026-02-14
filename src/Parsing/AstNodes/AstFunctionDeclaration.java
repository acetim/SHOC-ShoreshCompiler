package Parsing.AstNodes;

import Tokenization.TokenList;
import Tokenization.Tokenizer;

import java.util.ArrayList;

public class AstFunctionDeclaration extends AstStatement{
    private TokenList returnType;
    private String name;
    private ArrayList<AstParameter> parameters;
    private AstCodeBlock body;

    public AstFunctionDeclaration(TokenList returnType, ArrayList<AstParameter> parameters, String name, AstCodeBlock body) {
        super(TokenList.FUNCTION_DECLERATION);
        this.returnType = returnType;
        this.parameters = parameters;
        this.name = name;
        this.body = body;
    }

    public TokenList getReturnType() {
        return returnType;
    }

    public AstCodeBlock getBody() {
        return body;
    }

    public ArrayList<AstParameter> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }
}
