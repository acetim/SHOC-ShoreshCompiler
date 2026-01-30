package Parsing.AstNodes;

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
}
