package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstPrintIntStatement extends AstStatement{
    private final AstExpression expressionToPrint;

    public AstPrintIntStatement(AstExpression expressionToPrint) {
        super(TokenList.PRINT_INT);
        this.expressionToPrint = expressionToPrint;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstPrintIntStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"PRINT_INT ");
        this.expressionToPrint.print(indent);
        System.out.println();
    }

    public AstExpression getExpressionToPrint() {
        return expressionToPrint;
    }
}
