package Parsing.AstNodes;

import SemanticValidation.Visitor;

import java.util.ArrayList;

public class AstCodeBlock extends AstElement{
    private final ArrayList<AstStatement> Statements;

    public AstCodeBlock(ArrayList<AstStatement> statements) {
        Statements = statements;
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
