package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstIfStatement extends AstStatement{
    private final AstExpression condition;
    private final AstCodeBlock trueBlock;

    public AstIfStatement(AstExpression condition, AstCodeBlock trueBlock) {
        super(TokenList.KEYWORD_IF);
        this.condition = condition;
        this.trueBlock = trueBlock;
    }


    public AstExpression getCondition() {
        return condition;
    }


    public AstCodeBlock getTrueBlock() {
        return trueBlock;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstIfStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"if:");
        condition.print(indent);
        System.out.println();
        trueBlock.print(indent+"    ");
    }
}
