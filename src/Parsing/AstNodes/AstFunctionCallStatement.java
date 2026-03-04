package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;


/*
USED FOR VOID FUNCTION CALLS
 */
public class AstFunctionCallStatement extends AstStatement{


    private final AstFunctionExpression func;
    public AstFunctionCallStatement(AstFunctionExpression func) {
        super(TokenList.FUNCTION_CALL);
        this.func=func;
    }

    public AstFunctionExpression getFunc() {
        return func;
    }
    public String getName(){
        return this.func.getName();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstFunctionCallStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"call " +this.getName()+" args=");
        for(AstExpression exp :this.func.getArguments()){
            exp.print(indent);
        }
        System.out.println();
    }
}
