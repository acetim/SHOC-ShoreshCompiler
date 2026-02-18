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
        this.BindingPowers.put(TokenList.OPERATOR_PLUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MINUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MULTIPLIE,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_DEVIDES,new BindingPower(2.0,2.1));
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
                System.err.println("זוהתה בעיה דיקדוקית! נכנס למצב פאניקה לגילוי חטאים נוספים");
                this.Sync();//jump to next valid statement
            }
        }
        if(IsNestedParse) {
            throw new EofException("צדיק שחכת להוסיף 'ויתם' בסוף הפרק! המתחיל במצווה – אומרים לו גמור ");
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
            default -> {
                this.errorHandler.reportError("צדיק, זוהתה מילה לא נכונה אנא תקן!");
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
            this.errorHandler.reportError("צדיק שכחת להוסיף '=' בביטוי ההשוואה!");
        }
        next();
        AstExpression expression = ParseExpression();
        return new AstExpressionStatement(varName,expression);
    }

    private AstStatement ParseFunctionDeclaration()throws ParserException,EofException{
        /*
        gets called when the current token points to the ויהי keyword
        returns an ast tree of the function declaration of type AstFunctionDeclaration
         */
        this.next();
        if(!token.types.contains(this.getCurrentToken())&&this.getCurrentToken()!=TokenList.VOID){
            this.errorHandler.reportError("לכל מעשה מעשה יש תוצאה! - אנא ציין את סוג ההחזרה של המעשה");
        }
        TokenList returnType = this.getCurrentToken();

        this.next();
        if(this.getCurrentToken()!=TokenList.IDENTIFIER){
            this.errorHandler.reportError("לכל מעשה יש שם!- אנא ציין את שם המעשה");
        }
        String funcName = this.Tokens.get(this.CurrentToken).getValue();

        ArrayList<AstParameter> parameters= new ArrayList<>();
        this.next();
        if(this.getCurrentToken()!=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל '(' לאחר הכרזת שם הפונקציה");
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
        if(this.getCurrentToken()!=TokenList.IDENTIFIER){
            this.errorHandler.reportError("ציפה לשם פונקציה אחרי ויקרא");
        }
        String funcName = this.Tokens.get(this.CurrentToken).getValue();
        this.next();
        if(this.getCurrentToken()!=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError(" ציפה ל '(' אחרי שם הפונקציה");
        }
        this.next();
        ArrayList<AstExpression> args= new ArrayList<>();
        while(this.getCurrentToken()!=TokenList.CLOSING_ROUND_BRACKET){
            args.add(this.ParseExpression());
            this.next();
        }
        return new AstFunctionCallStatement(funcName,args);
    }

    private AstStatement ParseIntDeclaration() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.IDENTIFIER)){
            this.errorHandler.reportError("ציפיתי ממך לשם נורמלי למשתנה שהגדרת!");
        }
        String intName = this.Tokens.get(this.CurrentToken).getValue();
        this.next();
        if(CurrentTokenIsEqualTo(TokenList.SEMICOLON)){
            return new AstIntDeclaration(intName,32,null);
        }
        if(!CurrentTokenIsEqualTo(TokenList.OPERATOR_EQUALS)){
            this.errorHandler.reportError("הגדרת משתנה צריכה להגמר ב ';' או ב '=' ואז הגדרת הערך");
        }
        this.next();
        AstExpression expression = ParseExpression();//ends at semicolon token
        return new AstIntDeclaration(intName,32,expression);
    }

    private AstStatement ParseWhileStatement() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת בעוד");
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת בעוד");
        }
        this.next();
        AstCodeBlock CodeBlock =ParseCodeBlock();

        return new AstWhileStatement(Condition,CodeBlock);
    }

    private AstStatement ParseExit() throws ParserException,EofException{
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת ויהי חושך");
        }
        this.next();
        AstExpression ExitCode = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת ויהי חושך");
        }
        return new AstExitStatement(ExitCode);

    }

    private AstStatement ParseIfStatement() throws ParserException,EofException{//throw exceptions
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף '(' אחרי הכרזת אם יהיה");
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(!CurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("שחכת להוסיף ')' בסוף הכרזת אם יהיה");
        }
        this.next();
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock() throws ParserException,EofException{
        if(!CurrentTokenIsEqualTo(TokenList.OPENING_BRACKET)){
            this.errorHandler.reportError("ויעש צופה בתחילת הפרק");
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
            this.errorHandler.reportError("צדיק, שכחת להוסיף ';' בסוף הביטוי!");
        }
        return exp;
    }

    private AstExpression ParseFunctionExpression() throws ParserException,EofException{
        /*
        parses functionExpressions - used whe calling functions inside expressions
        or when calling functions that return void with 'ויעש'

        should get called on the identifier of a function
        returns an AstFunctionExpression that contains the arguments and the identifier token of the function
        sets the currentToken counter to be the closing round bracket of th call
         */
        token tok = this.Tokens.get(this.CurrentToken);
        ArrayList<AstExpression> args= new ArrayList<>();
        this.next();
        if(this.getCurrentToken()!=TokenList.OPENING_ROUND_BRACKET){
            this.errorHandler.reportError("ציפה ל '(' בקריאה לפונקציה");
        }
        this.next();
        while(this.getCurrentToken()!=TokenList.CLOSING_ROUND_BRACKET){
            args.add(this.ParseExpression());
            this.next();
        }
        return new AstFunctionExpression(tok,args);

    }

    private AstExpression PrattParse(Double min_bp) throws ParserException,EofException{

        AstExpression lhs=null;
        if(this.getCurrentToken()==TokenList.IDENTIFIER&&this.peek().getToken()==TokenList.OPENING_ROUND_BRACKET){
            lhs= ParseFunctionExpression();
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
                this.errorHandler.reportError("צדיק, שחכת לסגור סוגריים כמו שצריך בביטוי");
            }
        }
        else{
            this.errorHandler.reportError("צופה מספר או משתנה בביטוי");
        }

        while(true){//standard practice implementing pratt parsing
            if(this.peek().getToken()==TokenList.SEMICOLON||this.peek().getToken()==TokenList.CLOSING_ROUND_BRACKET) {//if detected a semicolon - end parsing
                break;
            }

            if(!isOp(this.peek().getToken())) {//if wrong Tokenization.token is detected throw error
                this.errorHandler.reportError("צופתה פעולה בביטוי");
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
            this.errorHandler.reportError("ציפה לסוג פרמטר בפונקציה");
        }
        TokenList type = this.getCurrentToken();
        this.next();
        if(this.getCurrentToken()!=TokenList.IDENTIFIER){
            this.errorHandler.reportError("ציפה לשם הארגומנט בפונקצייה");
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
        return (getCurrentToken() == TokenList.NUMBER || getCurrentToken() == TokenList.IDENTIFIER);
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
            throw new EofException("בעיה: הגעה לסוף הקובץ");
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
        System.err.println("!זוהו בעיות דקדוקיות!");
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
            System.out.println("בעיה: הגעה לסוף הקובץ");
            throw new RuntimeException(e);
        }
    }
    ///////////////////////////////////////////////////////////PARSER TESTING TODO REMOVE THIS SECTION


}


