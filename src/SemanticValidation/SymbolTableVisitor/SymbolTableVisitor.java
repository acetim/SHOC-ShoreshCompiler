package SemanticValidation.SymbolTableVisitor;

import Parsing.AstNodes.*;
import SemanticValidation.BasicComponents.SemanticErrorHandler;
import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;

import java.util.HashMap;

public class SymbolTableVisitor implements Visitor {
    private final SemanticErrorHandler errorHandler;
    private final GlobalSymbolTable globalSymbolTable;
    private SymbolTable currentScope;
    private int currentTotalStackOffset;
    private final HashMap<String,String> stringPool;
    private int stringCounter;
    public SymbolTableVisitor() {
        this.stringCounter=0;
        this.stringPool=new HashMap<>();
        this.errorHandler=new SemanticErrorHandler();
        this.currentTotalStackOffset=0;
        this.globalSymbolTable= new GlobalSymbolTable();
        this.currentScope=this.globalSymbolTable.getGlobalScope();
    }

    public void visit(AstCodeBlock root){
        root.accept(this);
        if(this.errorHandler.errorsPresent()){
            this.errorHandler.printAllErrors();
            System.exit(1);
        }
    }


    //TODO check if after each scope defining statement(while,if,funcDec) i backtrack and return the currentScope to be the parent
    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node){
        if(this.currentScope!=this.globalSymbolTable.getGlobalScope()){
            this.errorHandler.add("מעשה: "+node.getName()+" !הוגדר התוך מעשה אחר ");
        }

        FunctionSymbol functionSymbol = new FunctionSymbol(node.getReturnType());
        for(AstParameter p:node.getParameters()){
            functionSymbol.addParam(p);
        }
        String funcName = node.getName();
        if(this.globalSymbolTable.funcExists(funcName)){
            this.errorHandler.add("מעשה: "+node.getName()+" הוגדר פעמיים בקוד! ");
        }
        this.globalSymbolTable.defineFunc(functionSymbol,funcName);//add to global function registry

        //change scope to new function scope
        this.currentScope = new SymbolTable(currentScope);
        int offset = 16;

        for(AstParameter p:node.getParameters()){
            currentScope.addToTable(new Symbol(p.getName(),offset,p.getType()));
            offset += 8;
        }
        /*when visiting codeblock we need to check for invalid variable use (undefiened vars)
        while also adding the new vars to the current symbol table
        we also need to increment this.currentTotalStackOffset when adding to the symbol table
        and finally - set the codesblock scope to the current symbol table
         */
        int prevoffset=this.currentTotalStackOffset;//save prev offset for globals

        this.currentTotalStackOffset=0;//define new scope
        node.getBody().accept(this);

        node.setTotalStackOffset(this.currentTotalStackOffset);
        this.currentTotalStackOffset=prevoffset;//reset
        this.currentScope=currentScope.getParent();//reset scope to globals
    }

    @Override
    public void VisitAstCodeBlock(AstCodeBlock node) {
        for(AstStatement statement:node.getStatements()){
            statement.accept(this);
        }
        node.setScope(currentScope);
    }

    @Override
    public void VisitAstExpression(AstExpression node){//checks for undefined vars

        if(node.getToken().getToken()== TokenList.IDENTIFIER){//if var
           if(!this.currentScope.SymbolExists(node.getToken().getValue())){//check if var exists
               errorHandler.add("משתנה "+node.getToken().getValue()+" לא הוגדר");
           }
        }
        if(node.getLeft()!=null){node.getLeft().accept(this);}
        if(node.getRight()!=null){node.getRight().accept(this);}

    }

    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node){

        if(!this.globalSymbolTable.funcExists(node.getToken().getValue())){
            this.errorHandler.add("מעשה "+node.getToken().getValue()+" לא מוגדר");
        }

        for(AstExpression exp:node.getArguments()){//check for undefined args
            exp.accept(this);
        }
        //continue searching(doesnt really matter cuz funcExp will always be a leaf but whatever)
        if(node.getLeft()!=null){node.getLeft().accept(this);}
        if(node.getRight()!=null){node.getRight().accept(this);}
    }

    @Override
    public void VisitAstExitStatement(AstExitStatement node) {
        node.getExitCode().accept(this);
    }

    @Override
    public void VisitAstExpressionStatement(AstExpressionStatement node) {
        if(!this.currentScope.SymbolExists(node.getIdentifier())){
            errorHandler.add("משתנה "+node.getIdentifier()+" לא הוגדר");
        }
        node.getExpression().accept(this);

    }

    @Override
    public void VisitAstIfStatement(AstIfStatement node) {
        node.getCondition().accept(this);

        this.currentScope=new SymbolTable(this.currentScope);//create new scope

        node.getTrueBlock().accept(this);//visit codeblock
        this.currentScope=this.currentScope.getParent();//backtrack
    }

    @Override
    public void VisitAstWhileStatement(AstWhileStatement node) {
        node.getCondition().accept(this); //check for undefined vars

        this.currentScope = new SymbolTable(this.currentScope);//set new scope

        node.getCodeBlock().accept(this);//visit codeBlock
        this.currentScope=this.currentScope.getParent();
    }

    @Override
    public void VisitAstFunctionCallStatement(AstFunctionCallStatement node) {
        if(!this.globalSymbolTable.funcExists(node.getName())){
            this.errorHandler.add("מעשה "+node.getName()+" נקרא אך לא הוגדר ");
        }
        for(AstExpression param:node.getFunc().getArguments()){
            param.accept(this);
        }
    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {
        if(node.getReturnExpression()!=null) {
            node.getReturnExpression().accept(this);
        }
    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {
            if(this.currentScope==this.globalSymbolTable.getGlobalScope()){
                this.errorHandler.add(" משתנה "+node.getVarName()+" הוגדר מחוץ לפונקציה ");
            }
            this.currentTotalStackOffset -= 4;//ASSUMING ALL INT TYPES
            Symbol symbol = new Symbol(node.getVarName(), currentTotalStackOffset, TokenList.KEYWORD_INT);
            this.currentScope.addToTable(symbol);
            if(node.getExpression()!=null){
                node.getExpression().accept(this);
            }
    }

    @Override
    public void VisitAstInputStatement(AstInputStatement node) {
        String varName =node.getIdentifier().getValue();
        if(!this.currentScope.SymbolExists(varName)){
            this.errorHandler.add(" אי אפשר לקלוט פלט למשתנה "+varName+" כי אינו מוגדר");
        }
    }

    @Override
    public void VisitAstPrintIntStatement(AstPrintIntStatement node) {
        node.getExpressionToPrint().accept(this);
    }

    @Override
    public void VisitAstPrintStatement(AstPrintStatement node) {
        String str = node.getStringToPrint().getValue();
        if(!this.stringPool.containsKey(str)){
            this.stringPool.put(str,"ST"+this.stringCounter);
            this.stringCounter++;
        }
    }

    public GlobalSymbolTable getGlobalSymbolTable() {
        return globalSymbolTable;
    }

    public HashMap<String, String> getStringPool() {
        return stringPool;
    }
}
