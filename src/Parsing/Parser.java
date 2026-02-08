package Parsing;

import Exceptions.EofException;
import Exceptions.ParserException;
import Parsing.AstNodes.*;
import Tokenization.TokenList;
import Tokenization.token;

import java.util.ArrayList;
import java.util.HashMap;
public class Parser {
    private final ArrayList<token> Tokens;
    private int CurrentToken;
    private final HashMap<TokenList,BindingPower> BindingPowers;
    private final ParserErrorHandler errorHandler;
    private final ArrayList<AstStatement> allStatements;
    public Parser(ArrayList<token> tokens) {
        this.Tokens = tokens;
        this.CurrentToken = 0;
        this.BindingPowers=new HashMap<>();
        this.errorHandler= new ParserErrorHandler();
        this.allStatements=new ArrayList<>();
        //////////////////////////////////////////////MODIFY THIS AND operators IN TOKEN CLASS TO ADD/REMOVE OPERATORS IN THE PARSER
        this.BindingPowers.put(TokenList.OPERATOR_PLUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MINUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MULTIPLIE,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_DEVIDES,new BindingPower(2.0,2.1));
        //////////////////////////////////////////////MODIFY THIS AND operators IN TOKEN CLASS TO ADD/REMOVE OPERATORS IN THE PARSER

    }


    ///////////////////////////////////////////////////////////////ParserAPI
    public void Parse(){
        try {
            ParseLoop(false,this.allStatements);
            if(this.errorHandler.errorArePresent()){
                this.PrintSyntaxErrors();
                //TODO call system exit
            }
        }catch(EofException e){
            System.err.println("!EOF ERROR!");
            this.PrintSyntaxErrors();
            //TODO call system exit
        }
    }

    private void ParseLoop(boolean IsNestedParse,ArrayList<AstStatement> Statements) throws EofException{
        while(this.CurrentToken<this.Tokens.size()){
            try {
                if (IsNestedParse && this.getCurrentToken() == TokenList.CLOSING_BRACKET) {
                    return;
                }
                Statements.add(ParseStatement());
                this.CurrentToken++;
            } catch(ParserException e){
                System.out.println("Syntax error detected, entering panic mode to review all syntax errors");
                this.Sync();//jump to next valid statement
            }
        }
        if(IsNestedParse) {
            throw new EofException("expected '}' EOF REACHED WHILE PARSING");
        }
    }
    ///////////////////////////////////////////////////////////////ParserAPI



    ///////////////////////////////////////////////////////////////PARSE FUNCTIONS
    private AstStatement ParseStatement () throws ParserException,EofException{// returns null on comment statement !! HANDLE THIS !!

        switch (this.getCurrentToken()) {
            case TokenList.KEYWORD_EXIT -> {
                return this.ParseExit();
            }
            case TokenList.KEYWORD_IF -> {
                return this.ParseIfStatement();
            }
            case TokenList.KEYWORD_WHILE -> {
                return this.ParseWhileStatement();
            }
            case TokenList.KEYWORD_INT -> {
                return this.ParseIntDeclaration();
            }
            case TokenList.IDENTIFIER -> {
                return this.ParseExpressionStatement();
            }
            default -> {
                this.errorHandler.reportError("unexpected Tokenization.token detected");
                return null;
            }
        }
    }

    private AstStatement ParseExpressionStatement() throws ParserException,EofException{
        String varName =this.Tokens.get(this.CurrentToken).getValue();
        next();
        if(!CurrentTokenIsEqualTo(TokenList.OPERATOR_EQUALS)){
            this.errorHandler.reportError("expected '=' at expression statement");
        }
        next();
        AstExpression expression = ParseExpression();
        return new AstExpressionStatement(varName,expression);
    }

    private AstStatement ParseIntDeclaration() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.IDENTIFIER)){
            this.errorHandler.reportError("expected valid name for identifier after 'int' ");
        }
        String intName = this.Tokens.get(this.CurrentToken).getValue();
        this.next();
        if(CurrentTokenIsEqualTo(TokenList.SEMICOLON)){
            return new AstIntDeclaration(intName,32,null);
        }
        if(!CurrentTokenIsEqualTo(TokenList.OPERATOR_EQUALS)){
            this.errorHandler.reportError("expected ; or = for expression");
        }
        this.next();
        AstExpression expression = ParseExpression();//ends at semicolon token
        return new AstIntDeclaration(intName,32,expression);
    }

    private AstStatement ParseWhileStatement() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected '(' at while statement");
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected ')' at while statement");
        }
        this.next();
        AstCodeBlock CodeBlock =ParseCodeBlock();

        return new AstWhileStatement(Condition,CodeBlock);
    }

    private AstStatement ParseExit() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected '(' at exit statement");
        }
        this.next();
        AstExpression ExitCode = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected ')' at exit statement");
        }
        return new AstExitStatement(ExitCode);

    }

    private AstStatement ParseIfStatement() throws ParserException,EofException{//throw exceptions
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected '(' at if statement");
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected ')' at if statement");
        }
        this.next();
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock() throws ParserException,EofException{//should get the Tokenization.token that is the first { and return when encountering } and change the current pointer so it is one Tokenization.token after the end of the code block(main parser should have the same behavior)
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_BRACKET)){
            this.errorHandler.reportError("expected '{' for the code block");//HANDLE } AT PARSE LOOP
        }
        ArrayList<AstStatement> Statements = new ArrayList<>();
        this.next();
        ParseLoop(true,Statements);//returns when the current token is }
        return new AstCodeBlock(Statements);
    }

    private AstExpression ParseExpression () throws ParserException,EofException{
        /*
        should call func when the current token is the first atom of the exp
        returns the parsed expression tree
        sets this.currentToken to be the semicolon at the end of the exp
         */
        AstExpression exp = PrattParse(0.0);
        this.next();//end at the semicolon or send error if ')'
        if(this.getCurrentToken()==TokenList.CLOSING_ROUND_BRACKET){
            this.errorHandler.reportError("expected ';' at the end of the expression");
        }
        return exp;
    }

    private AstExpression PrattParse(Double min_bp) throws ParserException,EofException{

        AstExpression lhs=null;
        if(IsAtom()) {
            lhs = new AstExpression(Tokens.get(this.CurrentToken));
        }
        else if(CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            //parse parentheses
            this.next();
            lhs =PrattParse(0.0);
            this.next();
            if(this.getCurrentToken()==TokenList.SEMICOLON){
                this.errorHandler.reportError("expected ')' at expression");
            }
        }
        else{
            this.errorHandler.reportError("expected an atomic variable or value at expression");
        }

        while(true){//standard practice implementing pratt parsing
            if(this.peek().getToken()==TokenList.SEMICOLON||this.peek().getToken()==TokenList.CLOSING_ROUND_BRACKET) {//if detected a semicolon - end parsing
                break;
            }
            if(!isOp(this.peek().getToken())) {//if wrong Tokenization.token is detected throw error
                this.errorHandler.reportError("expected an operator at expression");
            }

            this.next();
            token op = this.Tokens.get(this.CurrentToken);//get operation

            //get binding powers
            Double l_bp = this.BindingPowers.get(op.getToken()).getLeft();
            Double r_bp = this.BindingPowers.get(op.getToken()).getRight();

            //if the current binding power of the operation is smaller than the previous, break and return lhs (no need to attach to weaker operation)
            if(l_bp<min_bp){break;}

            this.next();
            AstExpression rhs = PrattParse(r_bp);//if not recurse and keep exploring
            lhs = new AstExpression(op,lhs,rhs);
        }
        return lhs;
    }

    ///////////////////////////////////////////////////////////////PARSE FUNCTIONS



    /////////////////////////////////////////////////////////////helper functions
    private boolean CurrentTokenIsEqualTo(TokenList token){
        return getCurrentToken()==token;
    }

    private boolean IsAtom(){
        return (getCurrentToken() == TokenList.NUMBER || getCurrentToken() == TokenList.IDENTIFIER);
        }

    private TokenList getCurrentToken(){
        return this.Tokens.get(this.CurrentToken).getToken();
    }

    private token peek()throws EofException{
        if(this.CurrentToken<this.Tokens.size()-1) {
            return this.Tokens.get(this.CurrentToken + 1);
        }
        throw new EofException("EOF HAS BEEN REACHED");
    }

    private boolean isOp(TokenList t){
        return token.operators.contains(t);
    }

    private void next() throws EofException{
        this.CurrentToken++;
        if(this.CurrentToken>=this.Tokens.size()){
            throw new EofException("EOF HAS BEEN REACHED");
        }
    }

    ///////////////////////////////////////////////////////////helper functions



    ///////////////////////////////////////////////////////////panic mode
    private void Sync()throws EofException {
        do {
            this.next();//skip until a new statement is detected
        } while (!token.statements.contains(getCurrentToken()));
    }

    private void PrintSyntaxErrors(){
        System.err.println("!SYNTAX ERRORS DETECTED!");
        for(String s:this.errorHandler.getErrors()){
            System.err.println(s);
        }
    }
    ///////////////////////////////////////////////////////////panic mode



    ///////////////////////////////////////////////////////////PARSER TESTING TODO REMOVE THIS SECTION
    public AstExpression TEST___PARSE___EXPRESSION(){
        try {
            return this.ParseExpression();
        } catch (ParserException e) {
            this.PrintSyntaxErrors();
            throw new RuntimeException();
        } catch (EofException e) {
            System.out.println("EOF ERR");
            throw new RuntimeException(e);
        }
    }
    ///////////////////////////////////////////////////////////PARSER TESTING TODO REMOVE THIS SECTION


}


