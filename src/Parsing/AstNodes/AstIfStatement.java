package Parsing.AstNodes;

import SemanticValidation.Visitor;
import Tokenization.TokenList;

public class AstIfStatement extends AstStatement{
    private AstExpression condition;
    private AstCodeBlock trueBlock;

    public AstIfStatement(AstExpression condition, AstCodeBlock trueBlock) {
        super(TokenList.KEYWORD_IF);
        this.condition = condition;
        this.trueBlock = trueBlock;
    }

    public AstIfStatement() {
        super(TokenList.KEYWORD_IF);
        this.condition = null;
        this.trueBlock = null;
    }

    public AstExpression getCondition() {
        return condition;
    }

    public void setCondition(AstExpression condition) {
        this.condition = condition;
    }

    public AstCodeBlock getTrueBlock() {
        return trueBlock;
    }

    public void setTrueBlock(AstCodeBlock trueBlock) {
        this.trueBlock = trueBlock;
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
