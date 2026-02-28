package SemanticValidation.SymbolTableVisitor;

import java.util.HashMap;

public class SymbolTable {
    private SymbolTable parent;
    private HashMap<String,Symbol> symbolTable;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.symbolTable = new HashMap<>();
    }

    public boolean SymbolExists(String identifier){
        if(this.symbolTable.containsKey(identifier)){
            return true;
        }
        if(this.parent==null){
            return false;
        }
        return this.parent.SymbolExists(identifier);
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void addToTable(Symbol s){
        this.symbolTable.put(s.getName(),s);
    }
}
