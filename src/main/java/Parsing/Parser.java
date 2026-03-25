package Parsing;

import Exceptions.EofException;
import Exceptions.ParserException;
import Parsing.AstNodes.*;
import Tokenization.TokenList;
import Tokenization.token;

import java.util.ArrayList;
import java.util.HashMap;

/*
parser is o(n) time complexity where n is the number of tokens it gets in Tokens
 */
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
        //TODO add not
        this.BindingPowers.put(TokenList.OPERATOR_OR,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_AND,new BindingPower(1.0,1.1));

        this.BindingPowers.put(TokenList.OPERATOR_EQUALS,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_GREATERTHAN,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_SMALLERTHAN,new BindingPower(2.0,2.1));

        this.BindingPowers.put(TokenList.OPERATOR_PLUS,new BindingPower(3.0,3.1));
        this.BindingPowers.put(TokenList.OPERATOR_MINUS,new BindingPower(3.0,3.1));

        this.BindingPowers.put(TokenList.OPERATOR_MULTIPLIE,new BindingPower(4.0,4.1));
        this.BindingPowers.put(TokenList.OPERATOR_DEVIDES,new BindingPower(4.0,4.1));
        //////////////////////////////////////////////MODIFY THIS AND operators IN TOKEN CLASS TO ADD/REMOVE OPERATORS IN THE PARSER
        if(this.Tokens.getFirst().getToken()!=TokenList.BASAD){
            System.err.println("!!!!!!!!!!!!!!!!!אנא הוסף בסד בתחילת התוכנית!!!!!!!!!!!!!!!!!");
            System.exit(1);
        }
        this.Tokens.removeFirst();
    }


    ///////////////////////////////////////////////////////////////ParserAPI
    public void Parse(){
        try {
            ParseLoop(false,this.allStatements);
            if(this.errorHandler.errorArePresent()){
                this.PrintSyntaxErrors();
                System.exit(1);
            }
            else{
                System.out.println("\u001B[32m" + "בדיקה דיקדוקית הסתיימה בהצלחה" + "\u001B[0m");
            }
        }catch(EofException e){
            System.err.println(e.getMessage());
            this.PrintSyntaxErrors();
            System.exit(1);
        }
    }

    public ArrayList<AstStatement> getAllStatements(){
        return this.allStatements;
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
                this.next();
                this.Sync();//jump to next valid statement
            }
        }
        if(IsNestedParse) {
            throw new EofException("צדיק שכחת להוסיף 'ויתם' בסוף הפרק! המתחיל במצווה – אומרים לו גמור ");
        }
    }
    ///////////////////////////////////////////////////////////////ParserAPI



    ///////////////////////////////////////////////////////////////PARSE FUNCTIONS


    private AstStatement ParseStatement () throws ParserException,EofException{// returns null on comment statement !! HANDLE THIS !!
        /*
        every Parse func should set the current token to be the last token of the Statement
         */

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
            case TokenList.FUNCTION_DECLERATION -> {
                return this.ParseFunctionDeclaration();
            }
            case TokenList.FUNCTION_CALL -> {
                return this.ParseFunctionCallStatement();
            }
            case TokenList.KEYWORD_RETURN -> {
                return this.ParseReturnStatement();
            }
            case TokenList.PRINT_INT -> {
                return this.ParsePrintIntStatement();
            }
            case TokenList.PRINT_STRING -> {
                return this.ParsePrintStatement();
            }
            case TokenList.INPUT -> {
                return this.ParseInputStatement();
            }
            default -> {
                this.errorHandler.reportError(" צדיק, זוהתה מילה ואלידית אך מקומה לא נכון! ",this.getCurrentLine());
                return null;
            }
        }
    }

    private AstStatement ParseReturnStatement() throws ParserException,EofException{
        this.next();
        if(this.getCurrentToken()==TokenList.SEMICOLON){
            return new AstReturnStatement(null);
        }
        AstExpression exp = ParseExpression();
        return new AstReturnStatement(exp);
    }

    private AstStatement ParseExpressionStatement() throws ParserException,EofException{
        String varName =this.Tokens.get(this.CurrentToken).getValue();
        next();
        if(!CurrentTokenIsEqualTo(TokenList.OPERATOR_EQUALS)){
            this.errorHandler.reportError("צדיק שכחת להוסיף '=' בביטוי ההשוואה!",this.getCurrentLine());
        }
        next();
        AstExpression expression = ParseExpression();
        return new AstExpressionStatement(varName,expression);
    }

    private AstStatement ParsePrintStatement() throws ParserException,EofException{
        /*
        should get called on the first print token
        returns the parsed ast structure
        sets the current token to be the closing round bracket at the end of the statement
         */
        this.next();
        if (this.getCurrentToken() !=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל ( אחרי ויאמר",this.getCurrentLine());
        }
        this.next();
        if (this.getCurrentToken() !=TokenList.STRING_LITERAL){
            this.errorHandler.reportError("ציפה לקבל מחרוזת לאחר בתוך ויאמר",this.getCurrentLine());
        }
        token string_literal = this.Tokens.get(this.CurrentToken);
        this.next();
        if (this.getCurrentToken() !=TokenList.CLOSING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל ) אחרי ויאמר",this.getCurrentLine());
        }
        return new AstPrintStatement(string_literal);
    }

    private AstStatement ParsePrintIntStatement() throws ParserException,EofException{
        /*
        should get called on the first print token
        returns the parsed ast structure
        sets the current token to be the closing round bracket at the end of the statement
         */
        this.next();
        if (this.getCurrentToken() !=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל ( אחרי ויאמר",this.getCurrentLine());
        }
        this.next();
        AstExpression exp = ParseExpression();
        this.next();
        if (this.getCurrentToken() !=TokenList.CLOSING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל ) אחרי ויאמר",this.getCurrentLine());
        }
        return new AstPrintIntStatement(exp);
    }

    private AstStatement ParseInputStatement()throws ParserException,EofException{
        this.next();
        if (this.getCurrentToken() !=TokenList.IDENTIFIER){
            this.errorHandler.reportError("ציפה לשם משתנה לקריאת קלט",this.getCurrentLine());
        }
        token identifier = this.Tokens.get(this.CurrentToken);
        return new AstInputStatement(identifier);
    }

    private AstStatement ParseFunctionDeclaration()throws ParserException,EofException{
        /*
        gets called when the current token points to the ויהי keyword
        returns an ast tree of the function declaration of type AstFunctionDeclaration
         */
        this.next();
        if(!token.types.contains(this.getCurrentToken())&&this.getCurrentToken()!=TokenList.VOID){
            this.errorHandler.reportError("לכל מעשה מעשה יש תוצאה! - אנא ציין את סוג ההחזרה של המעשה",this.getCurrentLine());
        }
        TokenList returnType = this.getCurrentToken();

        this.next();
        if(this.getCurrentToken()!=TokenList.IDENTIFIER){
            this.errorHandler.reportError("לכל מעשה יש שם!- אנא ציין את שם המעשה",this.getCurrentLine());
        }
        String funcName = this.Tokens.get(this.CurrentToken).getValue();

        ArrayList<AstParameter> parameters= new ArrayList<>();
        this.next();
        if(this.getCurrentToken()!=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל '(' לאחר הכרזת שם הפונקציה",this.getCurrentLine());
        }
        this.next();
        while(this.getCurrentToken()!=TokenList.CLOSING_ROUND_BRACKET){
           parameters.add(this.ParseParameter());
           this.next();
        }
        this.next();
        AstCodeBlock functionBody = this.ParseCodeBlock();

        return new AstFunctionDeclaration(returnType,parameters,funcName,functionBody);

    }

    private AstStatement ParseFunctionCallStatement()throws ParserException,EofException{
        /*
        gets called when current token is FUNCTION_CALL
        returns AstFunctionCallStatement
        sets the current token to be the last token of the expression
         */
        this.next();
        AstFunctionExpression func = this.ParseFunctionExpression(true);
        return new AstFunctionCallStatement(func);
    }

    private AstStatement ParseIntDeclaration() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.IDENTIFIER)){
            this.errorHandler.reportError("ציפיתי ממך לשם נורמלי למשתנה שהגדרת!",this.getCurrentLine());
        }
        String intName = this.Tokens.get(this.CurrentToken).getValue();
        this.next();
        if(CurrentTokenIsEqualTo(TokenList.SEMICOLON)){
            return new AstIntDeclaration(intName,32,null);
        }
        if(!CurrentTokenIsEqualTo(TokenList.OPERATOR_EQUALS)){
            this.errorHandler.reportError("הגדרת משתנה צריכה להגמר ב ';' או ב '=' ואז הגדרת הערך",this.getCurrentLine());
        }
        this.next();
        AstExpression expression = ParseExpression();//ends at semicolon token
        return new AstIntDeclaration(intName,32,expression);
    }

    private AstStatement ParseWhileStatement() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת בעוד",this.getCurrentLine());
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת בעוד",this.getCurrentLine());
        }
        this.next();
        AstCodeBlock CodeBlock =ParseCodeBlock();

        return new AstWhileStatement(Condition,CodeBlock);
    }

    private AstStatement ParseExit() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת ויהי חושך",this.getCurrentLine());
        }
        this.next();
        AstExpression ExitCode = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת ויהי חושך",this.getCurrentLine());
        }
        return new AstExitStatement(ExitCode);

    }

    private AstStatement ParseIfStatement() throws ParserException,EofException{//throw exceptions
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת אם יהיה",this.getCurrentLine());
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת אם יהיה",this.getCurrentLine());
        }
        this.next();
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock() throws ParserException,EofException{
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_BRACKET)){
            this.errorHandler.reportError("ויעש צופה בתחילת הפרק",this.getCurrentLine());
        }
        ArrayList<AstStatement> Statements = new ArrayList<>();
        this.next();
        this.ParseLoop(true,Statements);//returns when the current token is }
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
            this.errorHandler.reportError("צדיק, שכחת להוסיף ';' בסוף הביטוי!",this.getCurrentLine());
        }
        return exp;
    }

    private AstFunctionExpression ParseFunctionExpression(boolean VoidExpected) throws ParserException,EofException{
        /*
        parses functionExpressions - used whe calling functions inside expressions
        or when calling functions that return void with 'ויעש'

        should get called on the identifier of a function
        returns an AstFunctionExpression that contains the arguments and the identifier token of the function
        sets the currentToken counter to be the closing round bracket of the call
         */
        token tok = this.Tokens.get(this.CurrentToken);
        ArrayList<AstExpression> args= new ArrayList<>();
        this.next();
        if(this.getCurrentToken()!=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל '(' בקריאה לפונקציה",this.getCurrentLine());
        }
        this.next();
        while(this.getCurrentToken()!=TokenList.CLOSING_ROUND_BRACKET){
            args.add(this.ParseExpression());
            this.next();
        }
        return new AstFunctionExpression(tok,args,VoidExpected);

    }

    private AstExpression PrattParse(Double min_bp) throws ParserException,EofException{

        AstExpression lhs=null;
        if(this.getCurrentToken()==TokenList.IDENTIFIER&&this.peek().getToken()==TokenList.OPENING_ROUND_BRACKET){
            lhs= ParseFunctionExpression(false);
        }
        else if(this.IsUnaryOp(this.getCurrentToken())){
            token Unary_Token = this.Tokens.get(this.CurrentToken);
            this.next();
            lhs = new AstExpression(Unary_Token,PrattParse(1000.0),null);//create new ast expression node with a single branch
        }
        else if(IsAtom()) {
            lhs = new AstExpression(Tokens.get(this.CurrentToken));
        }
        else if(CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){

            //parse parentheses
            this.next();
            lhs =PrattParse(0.0);
            this.next();
            if(this.getCurrentToken()==TokenList.SEMICOLON){
                this.errorHandler.reportError("צדיק, שחכת לסגור סוגריים כמו שצריך בביטוי",this.getCurrentLine());
            }
        }
        else{
            this.errorHandler.reportError("צופה מספר או משתנה בביטוי",this.getCurrentLine());
        }

        while(true){//standard practice implementing pratt parsing
            if(this.peek().getToken()==TokenList.SEMICOLON||this.peek().getToken()==TokenList.CLOSING_ROUND_BRACKET) {//if detected a semicolon - end parsing
                break;
            }

            if(!isOp(this.peek().getToken())) {//if wrong main.java.Tokenization.token is detected throw error
                this.errorHandler.reportError("צופתה פעולה בביטוי",this.getCurrentLine());
            }


            token op = this.peek();//get operation

            //get binding powers
            Double l_bp = this.BindingPowers.get(op.getToken()).getLeft();
            Double r_bp = this.BindingPowers.get(op.getToken()).getRight();

            //if the current binding power of the operation is smaller than the previous, break and return lhs (no need to attach to weaker operation)
            if(l_bp<min_bp){break;}
            this.next();
            this.next();
            AstExpression rhs = PrattParse(r_bp);//if not recurse and keep exploring
            lhs = new AstExpression(op,lhs,rhs);
        }

        return lhs;
    }

    private AstParameter ParseParameter()throws ParserException,EofException{
        if(!token.types.contains(this.getCurrentToken())){
            this.errorHandler.reportError("ציפה לסוג פרמטר בפונקציה",this.getCurrentLine());
        }
        TokenList type = this.getCurrentToken();
        this.next();
        if(this.getCurrentToken()!=TokenList.IDENTIFIER){
            this.errorHandler.reportError("ציפה לשם הארגומנט בפונקצייה",this.getCurrentLine());
        }
        String name = this.Tokens.get(this.CurrentToken).getValue();
        return new AstParameter(type,name);
    }

    ///////////////////////////////////////////////////////////////PARSE FUNCTIONS



    /////////////////////////////////////////////////////////////helper functions
    private boolean CurrentTokenIsEqualTo(TokenList token){
        return getCurrentToken()==token;
    }

    private boolean IsAtom(){
        return token.atoms.contains(this.getCurrentToken());
    }

    private TokenList getCurrentToken(){
        return this.Tokens.get(this.CurrentToken).getToken();
    }

    private token peek()throws EofException{
        if(this.CurrentToken<this.Tokens.size()-1) {
            return this.Tokens.get(this.CurrentToken + 1);
        }
        throw new EofException("בעיה: הגעה לסוף הקובץ");
    }

    private boolean isOp(TokenList t){
        return token.operators.contains(t);
    }

    private void next() throws EofException{
        this.CurrentToken++;
        if(this.CurrentToken>=this.Tokens.size()){
            throw new EofException("");
        }
    }

    private int getCurrentLine(){
        return this.Tokens.get(this.CurrentToken).getLine();
    }

    private boolean IsUnaryOp(TokenList t){
        return token.Unary_Operators.contains(t);
    }
    ///////////////////////////////////////////////////////////helper functions



    ///////////////////////////////////////////////////////////panic mode
    private void Sync()throws EofException {
        int nestCounter=0;
        do {
            if(this.getCurrentToken()==TokenList.OPENING_ROUND_BRACKET){
                nestCounter+=1;
            }
            if(this.getCurrentToken()==TokenList.CLOSING_BRACKET){
                if(nestCounter==0){return;}
                nestCounter-=1;
            }
            this.next();//skip until a new statement is detected
        } while (!token.statements.contains(getCurrentToken()));
    }

    private void PrintSyntaxErrors(){
        System.err.println("!זוהו בעיות דקדוקיות!");
        for(String s:this.errorHandler.getErrors()){
            System.err.println(s);
        }
    }
    ///////////////////////////////////////////////////////////panic mode



}


