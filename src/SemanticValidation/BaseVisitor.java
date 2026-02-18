package SemanticValidation;

import Parsing.AstNodes.*;

public abstract class BaseVisitor implements Visitor{

    public BaseVisitor() {
    }

    @Override
    public void VisitAstExpressionStatement(AstExpressionStatement node) {

    }

    @Override
    public void VisitAstIfStatement(AstIfStatement node) {

    }

    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node) {

    }

    @Override
    public void VisitAstWhileStatement(AstWhileStatement node) {

    }

    @Override
    public void VisitAstCodeBlock(AstCodeBlock node) {

    }

    @Override
    public void VisitAstExpression(AstExpression node) {

    }

    @Override
    public void VisitAstExitStatement(AstExitStatement node) {

    }

    @Override
    public void VisitAstFunctionCallStatement(AstFunctionCallStatement node) {

    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {

    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {

    }


    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node) {

    }
}
