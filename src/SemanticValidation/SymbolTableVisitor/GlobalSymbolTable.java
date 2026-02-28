package SemanticValidation.SymbolTableVisitor;

import java.util.HashMap;

public class GlobalSymbolTable {
    private SymbolTable globals;
    private HashMap<String,FunctionSymbol> functions;//global function table

    public GlobalSymbolTable() {
        this.globals = new SymbolTable(null);//no parent (root symbol table)
        this.functions = new HashMap<>();
    }
    public boolean funcExists(String s){
        return this.functions.containsKey(s);
    }
    public FunctionSymbol getFunc(String s) {
            return this.functions.get(s);
    }

    public void defineFunc(FunctionSymbol fs,String name){
        this.functions.put(name,fs);
    }


    public SymbolTable getGlobalScope() {
        return globals;
    }

    public HashMap<String, FunctionSymbol> getFunctions() {
        return functions;
    }
}
