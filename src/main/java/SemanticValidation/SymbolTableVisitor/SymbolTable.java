package SemanticValidation.SymbolTableVisitor;

import java.util.HashMap;

public class SymbolTable {
    private final SymbolTable parent;
    private final HashMap<String,Symbol> symbolTable;

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

    public String getOffsetStr(String identifier){
        if(this.symbolTable.containsKey(identifier)){
            return this.symbolTable.get(identifier).getOffsetStr();
        }
        if(this.parent==null){
            throw new RuntimeException(identifier+" NOT FOUND");
        }
        return this.parent.getOffsetStr(identifier);
    }


public SymbolTable getParent() {
    return parent;
}

public void addToTable(Symbol s){
    this.symbolTable.put(s.getName(),s);
}
public void printSymbolTable(){
    for(Symbol s: this.symbolTable.values()){
        System.out.println(s.toString());
    }
}
}
