package SemanticValidation;

import java.util.HashMap;

public class SymbolTable {
    private SymbolTable parent;
    private HashMap<String,Symbol> symbolTable;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.symbolTable = new HashMap<>();
    }

    public boolean SymbolExists(String identifier){
        if (parent!=null) {
            if (this.symbolTable.containsKey(identifier)) {
                return true;
            }
            return this.parent.SymbolExists(identifier);
        }
        return false;
    }

    public SymbolTable getParent() {
        return parent;
    }

    public void addToTable(Symbol s){
        this.symbolTable.put(s.getName(),s);
    }
}
