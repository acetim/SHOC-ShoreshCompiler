package Parsing.AstNodes;

import SemanticValidation.Visitor;
import Tokenization.TokenList;


import java.util.ArrayList;

public class AstFunctionDeclaration extends AstStatement{
    private final TokenList returnType;
    private final String name;
    private final ArrayList<AstParameter> parameters;
    private final AstCodeBlock body;

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

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstFunctionDeclaration(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"func "+this.returnType.name()+" "+this.name+"( ");
        for(AstParameter par : this.parameters){
            par.print(indent);
        }
        System.out.println(")");
        this.body.print(indent+"    ");
    }

    public String getName() {
        return name;
    }
}
