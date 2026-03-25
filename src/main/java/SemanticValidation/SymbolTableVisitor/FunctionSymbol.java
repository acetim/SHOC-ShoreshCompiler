package SemanticValidation.SymbolTableVisitor;

import java.util.ArrayList;
import Parsing.AstNodes.AstParameter;
import Tokenization.TokenList;
/*
funcs in asm will be named:
FUNC0
FUNC1
FUNC2
...
 */
public class FunctionSymbol {
    private String funcName;
    private final ArrayList<AstParameter> parameters;
    private final TokenList returnType;

    public FunctionSymbol( TokenList returnType) {
        this.parameters = new ArrayList<>();
        this.returnType = returnType;
    }

    public void addParam(AstParameter a){
        this.parameters.add(a);
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public ArrayList<AstParameter> getParameters() {
        return parameters;
    }

    public TokenList getReturnType() {
        return returnType;
    }

    public String getFuncName() {
        return funcName;
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
