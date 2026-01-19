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
}
