package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;
import Tokenization.token;

public class AstInputStatement extends AstStatement{
    private final token identifier;

    public AstInputStatement( token identifier) {
        super(TokenList.INPUT);
        this.identifier = identifier;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent+"INPUT "+this.identifier.getValue());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstInputStatement(this);
    }

    public token getIdentifier() {
        return identifier;
    }
}
