package SemanticValidation.BasicComponents;

import Parsing.AstNodes.*;

public interface Visitor {
    void VisitAstIfStatement(AstIfStatement node);

    void VisitAstFunctionDeclaration(AstFunctionDeclaration node);

    void VisitAstWhileStatement(AstWhileStatement node);

    void VisitAstCodeBlock(AstCodeBlock node);

    void VisitAstExpression(AstExpression node);

    void VisitAstExitStatement(AstExitStatement node);

    void VisitAstFunctionCallStatement(AstFunctionCallStatement node);

    void VisitAstReturnStatement(AstReturnStatement node);

    void VisitAstIntDeclaration(AstIntDeclaration node);

    void VisitAstFunctionExpression(AstFunctionExpression node);

    void VisitAstExpressionStatement(AstExpressionStatement node);


}