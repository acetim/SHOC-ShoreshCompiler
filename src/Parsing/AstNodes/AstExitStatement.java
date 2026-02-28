package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

public class AstExitStatement extends AstStatement{
    private AstExpression ExitCode;

    public AstExitStatement( AstExpression exitCode) {
        super(TokenList.KEYWORD_EXIT);
        ExitCode = exitCode;
    }

    public AstExpression getExitCode() {
        return ExitCode;
    }

    public void setExitCode(AstExpression exitCode) {
        ExitCode = exitCode;
    }

    @Override
    public void print(String indent) {
        System.out.print(indent+"EXIT:");
        ExitCode.print(indent);
        System.out.println();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstExitStatement(this);
    }
}
