package SemanticValidation.SymbolTableVisitor;

import Parsing.AstNodes.*;
import SemanticValidation.BasicComponents.SemanticErrorHandler;
import SemanticValidation.BasicComponents.TypeTable;
import SemanticValidation.BasicComponents.Visitor;
import Tokenization.TokenList;
public class SymbolTableVisitor implements Visitor {
    private SemanticErrorHandler errorHandler;
    private GlobalSymbolTable globalSymbolTable;
    private SymbolTable currentScope;
    private int currentTotalStackOffset;
    public SymbolTableVisitor() {
        this.errorHandler=new SemanticErrorHandler();
        this.currentTotalStackOffset=0;
        this.globalSymbolTable= new GlobalSymbolTable();
        this.currentScope=this.globalSymbolTable.getGlobalScope();
    }
    //TODO check if after each scope defining statement(while,if,funcDec) i backtrack and return the currentScope to be the parent
    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node){
        if(this.currentScope!=this.globalSymbolTable.getGlobalScope()){
            this.errorHandler.add("!הוגדר התוך מעשה אחר "+node.getName()+" מעשה:");
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
            offset += TypeTable.TypeTable.get(p.getType());
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
               errorHandler.add("לא הוגדר "+node.getToken().getValue()+" משתנה");
           }
        }
        if(node.getLeft()!=null){node.getLeft().accept(this);}
        if(node.getRight()!=null){node.getRight().accept(this);}

    }

    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node){
        if(this.globalSymbolTable.funcExists(node.getToken().getValue())){
            this.errorHandler.add("לא מוגדר "+node.getToken().getValue()+" מעשה");
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
            errorHandler.add("לא הוגדר "+node.getIdentifier()+" משתנה");
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
        if(!this.globalSymbolTable.funcExists(node.getFuncName())){
            this.errorHandler.add("נקרא אך לא הוגדר"+node.getFuncName()+" מעשה");
        }
        for(AstExpression param:node.getArgs()){
            param.accept(this);
        }
    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {
        node.getReturnExpression().accept(this);
    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {
        this.currentTotalStackOffset-=4;//ASSUMING ALL INT TYPES
        Symbol symbol = new Symbol(node.getVarName(),currentTotalStackOffset,TokenList.KEYWORD_INT);
        this.currentScope.addToTable(symbol);
    }
}
