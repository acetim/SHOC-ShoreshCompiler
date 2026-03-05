package SemanticValidation.TypeCheckerVisitor;

import Parsing.AstNodes.*;
import SemanticValidation.BasicComponents.SemanticErrorHandler;
import SemanticValidation.BasicComponents.Visitor;
import SemanticValidation.SymbolTableVisitor.FunctionSymbol;
import SemanticValidation.SymbolTableVisitor.GlobalSymbolTable;
import Tokenization.TokenList;
import Tokenization.token;

//ASSUMING ALL INT TYPES!!!!!
public class TypeCheckerVisitor implements Visitor {
    private final GlobalSymbolTable globalSymbolTable;
    private FunctionSymbol currentFunc;
    private final SemanticErrorHandler errorHandler;
    private String currentFuncName;
    public TypeCheckerVisitor(GlobalSymbolTable globalSymbolTable) {
        this.globalSymbolTable = globalSymbolTable;
        this.errorHandler= new SemanticErrorHandler();
        this.currentFunc=null;
        this.currentFuncName = "";
    }
    public void visit(AstCodeBlock root){
        root.accept(this);
        if(this.errorHandler.errorsPresent()){
            this.errorHandler.printAllErrors();
            System.exit(1);
        }
    }

    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node) {
        this.currentFunc = globalSymbolTable.getFunc(node.getName());
        this.currentFuncName=node.getName();

        boolean funcHasReturn= false;
        for(AstStatement statement :node.getBody().getStatements()){//check for return statement
            if(statement instanceof AstReturnStatement){
                funcHasReturn = true;
                break;
            }
        }
        if(this.currentFunc.getReturnType()!= TokenList.VOID&&!funcHasReturn){
            //exit if func is not of void type and does not return
            this.errorHandler.add("המעשה "+node.getName()+" לא מחזיר כלום ");
        }

        node.getBody().accept(this);
        this.currentFunc=null;
        this.currentFuncName="";
    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {
        //TODO implement this
        //no need,assuming all int types
        //implement this when introducing new types
    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {
        if (node.getReturnExpression()==null && this.currentFunc.getReturnType()!=TokenList.VOID){//if return is none but func isnt void
            this.errorHandler.add("צופה מאמן בתוך מעשה "+this.currentFuncName+" להחזיר ערך אך קיבל כלום ");
        }
        if(node.getReturnExpression()!=null){
            node.getReturnExpression().accept(this);
        }
        //TODO assuming all int types so no need to check
        //implement this when introducing new types
    }

    @Override
    public void VisitAstFunctionCallStatement(AstFunctionCallStatement node) {
        //ASSUMING ALL INT TYPES
        FunctionSymbol functionSymbol = this.globalSymbolTable.getFunc(node.getName());

        if(functionSymbol.getReturnType()!=TokenList.VOID){//check if func returns void
            this.errorHandler.add("אי אפשר לקרוא קריאת ויקרא על המעשה "+node.getName()+" שלא מחזיר תהו ובהו ");
        }

        node.getFunc().accept(this);
    }

    @Override
    public void VisitAstWhileStatement(AstWhileStatement node) {
        TokenList topLevelOperator = node.getCondition().getToken().getToken();
        //ASSUMING ALL INT TYPES
        if(!token.logicOps.contains(topLevelOperator)){//check if top level operator gives a boolean output
            this.errorHandler.add(":במעשה "+this.currentFuncName+" בעוד חייב לקבל ביוטי שמביא ערך בוליאני ");
        }
        node.getCodeBlock().accept(this);
    }

    @Override
    public void VisitAstIfStatement(AstIfStatement node) {
        TokenList topLevelOperator = node.getCondition().getToken().getToken();
        //ASSUMING ALL INT TYPES
        if(!token.logicOps.contains(topLevelOperator)){//check if top level operator gives a boolean output
            this.errorHandler.add(":במעשה "+this.currentFuncName+" בעוד חייב לקבל ביוטי שמביא ערך בוליאני ");
        }
        node.getTrueBlock().accept(this);
    }

    @Override
    public void VisitAstExitStatement(AstExitStatement node) {
        //NO NEED TO CHECK - ASSUMING ALL INT TYPES FOR NOW
    }

    @Override
    public void VisitAstExpressionStatement(AstExpressionStatement node) {
        //NO NEED TO CHECK - ASSUMING ALL INT TYPES FOR NOW
        node.getExpression().accept(this);
    }

    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node) {
        /*
        function expression can be declared in two scenarios-
        call statement - "ויקרא":which expects a void/!regular! return val
        and an expression which expects a return val
        this is why we check the ExpectsVoid var in node
         */

        FunctionSymbol funcSymbol = this.globalSymbolTable.getFunc(node.getName());
        //ASSUMING ALL INT TYPES!!!
        if(!node.isVoidExpected()&&funcSymbol.getReturnType()==TokenList.VOID){//err if - expression not expects void but gets void
            this.errorHandler.add(" במעשה"+this.currentFuncName+" נקרא המעשה "+node.getName()+"וצופה ממנו להחזיר ערך, אך מחזיר תהו ובהו ");
        }
        //ASSUMING ALL INT TYPES!!!
        if(funcSymbol.getParameters().size()!=node.getArguments().size()){
            this.errorHandler.add(" במעשה"+this.currentFuncName+" נקרא המעשה "+node.getName()+"אך כמות הפרמטרים שקיבל אינה נכונה ");
        }
        if(node.getLeft()!=null){node.getLeft().accept(this);}
        if(node.getRight()!=null){node.getRight().accept(this);}
    }

    @Override
    public void VisitAstExpression(AstExpression node) {
        //NO NEED TO CHECK - ASSUMING ALL INT TYPES FOR NOW
        //BUT NEED TO EXPLORE TO CHECK FOR FUNC EXPS
        if(node.getLeft()!=null){node.getLeft().accept(this);}
        if(node.getRight()!=null){node.getRight().accept(this);}
    }

    @Override
    public void VisitAstCodeBlock(AstCodeBlock node) {
        for(AstStatement statement:node.getStatements()){
            statement.accept(this);
        }
    }

}
