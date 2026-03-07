package SemanticValidation.SymbolTableVisitor;

import java.util.ArrayList;
import Parsing.AstNodes.AstParameter;
import Parsing.AstNodes.AstStatement;
import Tokenization.TokenList;

public class FunctionSymbol {
    private final ArrayList<AstParameter> parameters;
    private final TokenList returnType;

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

    public String toString(){
        String s ="params: ";
        for(AstParameter p: this.parameters){
            s+=p.getType().name()+" ";
            s+=p.getName()+"\n";
        }
        s+="return type: "+this.returnType.name();
        return s;

    }
}
