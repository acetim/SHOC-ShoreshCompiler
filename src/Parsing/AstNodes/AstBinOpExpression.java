//package Parsing.AstNodes;
//
//import Tokenization.TokenList;
//import Tokenization.token;
//
//public class AstBinOpExpression extends AstExpression{ //BINARY ONLY EXPRESSION SYSTEM!!
//    private AstExpression left;
//    private AstExpression right;
//    private TokenList operation;
//
//
//
//    public AstBinOpExpression(AstExpression left, AstExpression right,TokenList op) {
//        super(token.tokensToString.get(op));
//        this.left = left;
//        this.right = right;
//        this.operation =op;
//    }
//
//    public TokenList getOperation() {
//        return operation;
//    }
//
//    public void setOperation(TokenList operation) {
//        this.operation = operation;
//    }
//
//    public AstExpression getRight() {
//        return right;
//    }
//
//    public void setRight(AstExpression right) {
//        this.right = right;
//    }
//
//
//    public AstExpression getLeft() {
//        return left;
//    }
//
//    public void setLeft(AstExpression left) {
//        this.left = left;
//    }
//}
