package Parsing.AstNodes;

import java.util.ArrayList;

public class AstCodeBlock extends AstElement{
    private ArrayList<AstStatement> Statements;

    public AstCodeBlock(ArrayList<AstStatement> statements) {
        Statements = statements;
    }

    public ArrayList<AstStatement> getStatements() {
        return Statements;
    }
    public void print(String indent){
        PARSER_TESTING.printElements(indent,Statements);
    }
    public void setElements(ArrayList<AstStatement> Statements) {
        this.Statements = Statements;
    }
}
