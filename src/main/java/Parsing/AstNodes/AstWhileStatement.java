package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstWhileStatement extends AstStatement{
    private final AstExpression condition;
    private final AstCodeBlock codeBlock;

    public AstWhileStatement(AstExpression condition, AstCodeBlock codeBlock) {
        super(TokenList.KEYWORD_WHILE);
        this.condition = condition;
        this.codeBlock = codeBlock;
    }

    public AstExpression getCondition() {
        return condition;
    }

    public AstCodeBlock getCodeBlock() {
        return codeBlock;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstWhileStatement(this);
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"WHILE: ");
        condition.print(indent);
        System.out.println();
        codeBlock.print(indent+"    ");
    }
}
