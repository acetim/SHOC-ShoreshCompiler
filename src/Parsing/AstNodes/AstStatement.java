package Parsing.AstNodes;

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
}
