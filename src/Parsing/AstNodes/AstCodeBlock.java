package Parsing.AstNodes;

import SemanticValidation.SymbolTable;
import SemanticValidation.Visitor;

import java.util.ArrayList;

public class AstCodeBlock extends AstElement{
    private SymbolTable scope;
    private final ArrayList<AstStatement> Statements;

    public AstCodeBlock(ArrayList<AstStatement> statements) {
        Statements = statements;
    }

    public SymbolTable getScope() {
        return scope;
    }

    public void setScope(SymbolTable symbolTable) {
        this.scope = symbolTable;
    }
    public ArrayList<AstStatement> getStatements() {
        return Statements;
    }
    public void print(String indent){
        PARSER_TESTING.printElements(indent,Statements);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.VisitAstCodeBlock(this);
    }
}
