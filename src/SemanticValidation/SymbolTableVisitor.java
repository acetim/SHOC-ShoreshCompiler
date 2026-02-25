package SemanticValidation;

import Exceptions.SemanticException;
import Parsing.AstNodes.AstFunctionDeclaration;
import Parsing.AstNodes.AstParameter;

public class SymbolTableVisitor extends BaseVisitor{
    private GlobalSymbolTable globalSymbolTable;
    private SymbolTable currentScope;
    private int currentTotalStackOffset;
    public SymbolTableVisitor() {
        this.currentTotalStackOffset=0;
        this.globalSymbolTable= new GlobalSymbolTable();
        this.currentScope=this.globalSymbolTable.getGlobalScope();
    }

    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node) throws SemanticException {
        if(this.currentScope!=this.globalSymbolTable.getGlobalScope()){
            throw new SemanticException("אי אפשר להגדיר מעשה בתוך מעשה");
        }

        FunctionSymbol functionSymbol = new FunctionSymbol(node.getReturnType());
        for(AstParameter p:node.getParameters()){
            functionSymbol.addParam(p);
        }
        String funcName = node.getName();
        this.globalSymbolTable.defineFunc(functionSymbol,funcName);//add to global function registry

        //change scope to new function scope
        this.currentScope = new SymbolTable(currentScope);
        int offset = 8;

        for(AstParameter p:node.getParameters()){
            currentScope.addToTable(new Symbol(p.getName(),offset,p.getType()));
            offset +=4;//assuming all ints
        }
        /*when visiting codeblock we need to check for invalid variable use (undefiened vars)
        while also adding the new vars to the current symbol table
        we also need to increment this.currentTotalStackOffset when adding to the symbol table
        and finally - set the codesblock scope to the current symbol table
         */
        int prevoffset=this.currentTotalStackOffset;//save prev offset for globals

        this.currentTotalStackOffset=0;//define new scope
        this.VisitAstCodeBlock(node.getBody());

        node.setTotalStackOffset(this.currentTotalStackOffset);
        this.currentTotalStackOffset=prevoffset;//reset
        this.currentScope=currentScope.getParent();//reset scope to globals
    }
}
