package Parsing.AstNodes;

import Tokenization.token;

public class AstExpression extends AstElement{
    private final token Token;
    private final AstExpression left;
    private final AstExpression right;
    public AstExpression(token value) {
        this.Token = value;
        this.left=null;
        this.right=null;
    }
    public AstExpression(token value,AstExpression left, AstExpression right) {
        this.Token = value;
        this.left=left;
        this.right=right;
    }

    public AstExpression getLeft() {
        return left;
    }

    public AstExpression getRight() {
        return right;
    }

    public token getToken() {
        return Token;
    }

}
