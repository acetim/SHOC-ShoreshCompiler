package SemanticValidation;

import Exceptions.SemanticException;

import java.util.HashMap;

public class GlobalSymbolTable {
    private SymbolTable globals;
    private HashMap<String,FunctionSymbol> functions;//global function table

    public GlobalSymbolTable() {
        this.globals = new SymbolTable(null);//no parent (root symbol table)
        this.functions = new HashMap<>();
    }
    public FunctionSymbol lookupFunc(String s)throws SemanticException {
        if(this.functions.containsKey(s)){
            return this.functions.get(s);
        }
        throw new SemanticException("מעשה:" +s+"שאינו מוגדר נקרא");
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
