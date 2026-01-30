package Parsing.AstNodes;

import java.util.ArrayList;

public class AstCodeBlock extends AstElement{
    private ArrayList<AstStatement> Statements;

    public AstCodeBlock(ArrayList<AstStatement> Statements) {
        Statements = new ArrayList<AstStatement>();
    }

    public ArrayList<AstStatement> getStatements() {
        return Statements;
    }

    public void setElements(ArrayList<AstStatement> Statements) {
        this.Statements = Statements;
    }
}
