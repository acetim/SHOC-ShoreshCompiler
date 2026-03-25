package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;
import Tokenization.token;

public class AstPrintStatement extends AstStatement{
    private final token string;

    public AstPrintStatement(token string) {
        super(TokenList.PRINT);
        this.string = string;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent+"PRINT "+this.string.getValue());
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstPrintStatement(this);
    }

    public token getStringToPrint() {
        return string;
    }
}
