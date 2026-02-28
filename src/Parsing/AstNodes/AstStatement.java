package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstStatement extends AstElement{
    private TokenList StatementType;

    public AstStatement(TokenList statementType) {
        StatementType = statementType;
    }

    public TokenList getStatementType() {
        return StatementType;
    }

    public void setStatementType(TokenList statementType) {
        StatementType = statementType;
    }


    @Override
    public void accept(Visitor visitor) {
        System.err.println("how did we get here?");
        System.err.println("unimplemented accept() at"+ this.StatementType.name());
        System.exit(1);
    }

    @Override
    public void print(String indent) {
        System.out.println("how did we get here?");
    }

}
