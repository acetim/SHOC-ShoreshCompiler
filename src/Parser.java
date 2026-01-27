import java.util.ArrayList;
import java.util.HashMap;
public class Parser {
    private final ArrayList<token> Tokens;
    private int CurrentToken;
    private final HashMap<TokenList,BindingPower> BindingPowers;
    private final ParserErrorHandler errorHandler;
    private ArrayList<AstStatement> allStatements;
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
    /*
    handle ParserExceptions in the parse loop, print "entering panic mode" and at the end of parsing display all errors
    throw exceptions using reportError()

    when implementing the parse loop increment this.currentToken manually after each
    parse loop takes and handles end condition
     */



    /*
     TODO-RE MONITOR FLOW OF STATEMENT BLOCKS TO ENFORCE THEY END AT THE LAST TOKEN AND ASSUME
     */


    ///////////////////////////////////////////////////////////////ParserAPI
    private void ParseLoop(boolean IsNestedParse) throws EofException{
        while(this.CurrentToken<this.Tokens.size()){
            try {
                this.allStatements.add(ParseStatement());
                this.CurrentToken++;
                if (IsNestedParse && this.getCurrentToken() == TokenList.CLOSING_BRACKET) {
                    return;
                }
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
        switch(this.Tokens.get(CurrentToken).token)
        {
            case TokenList.KEYWORD_INT:
                //return this.ParseIntDeclaration();
            case TokenList.KEYWORD_EXIT:
                return this.ParseExit();
//            case TokenList.COMMENT:
//               return this.ParseComment();

        }
    }

    private AstStatement ParseExit() throws ParserException,EofException{
        this.next();
        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected '(' at exit statement");
        }
        this.next();
        AstExpression ExitCode = ParseExpression();
        this.next();
        if(checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected ')' at exit statement");
        }
        return new AstExitStatement(ExitCode);

    }

    private AstStatement ParseIfStatement() throws ParserException,EofException{//throw exceptions
        this.next();
        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected '(' at if statement");
        }
        this.next();
        AstExpression Condition = ParseExpression();
        this.next();
        if(checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            this.errorHandler.reportError("expected ')' at if statement");
        }
        this.next();
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock() throws ParserException {//should get the token that is the first { and return when encountering } and change the current pointer so it is one token after the end of the code block(main parser should have the same behavior)

        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_BRACKET)){
            this.errorHandler.reportError("expected '{' for the code block");//HANDLE } AT PARSE LOOP
        }
        ArrayList<AstStatement> Statements = new ArrayList<>();
        //TODO call parse loop (parse loop should take end condition in which if the parse loop encounters it after incrementing it returns and the original parse loop s called with null)
        //PARSE LOOP SHOULD RETURN TO Statements

        return new AstCodeBlock(Statements);
    }
    //when implementing this, check all usages of parse expression for correct usage
    //TODO TEST THIS FUNC
    //currently the parser returns when the current token is a semicolon marking the end of the expression
    private AstExpression ParseExpression () throws ParserException,EofException{
        AstExpression exp = PrattParse(0.0);
        this.next();//end at the semicolon
        return exp;
    }

    private AstExpression PrattParse(Double min_bp) throws ParserException,EofException{

        if(!IsAtom(this.CurrentToken)){
            this.errorHandler.reportError("expected an atomic variable or value at expression");
        }
        //create an atom
        AstExpression lhs = new AstAtomExpression(Tokens.get(this.CurrentToken).value,(getCurrentToken()==TokenList.IDENTIFIER));

        while(true){//standard practice implementing pratt parsing
            if(this.peek().token==TokenList.SEMICOLON) {//if detected a semicolon - end parsing
                break;
            }
            if(!isOp(this.peek().token)) {//if wrong token is detected throw error
                this.errorHandler.reportError("expected an operator at expression");
            }

            this.next();
            TokenList op = getCurrentToken();//get operation

            //get binding powers
            Double l_bp = this.BindingPowers.get(op).getLeft();
            Double r_bp = this.BindingPowers.get(op).getRight();

            //if the current binding power of the operation is smaller than the previous, break and return lhs (no need to attach to weaker operation)
            if(l_bp<min_bp){break;}

            this.next();
            AstExpression rhs = PrattParse(r_bp);//if not recurse and keep exploring
            lhs = new AstBinOpExpression(lhs,rhs,op);
        }
        return lhs;
    }
//    private AstStatement ParseComment()throws EofException{
//        this.next();
//        return null;
//    }
    ///////////////////////////////////////////////////////////////PARSE FUNCTIONS



    /////////////////////////////////////////////////////////////helper functions
    private boolean checkIfCurrentTokenIsEqualTo(TokenList token){
        return getCurrentToken()==token;
    }

    private boolean IsAtom(int TokenCount){
        if(TokenCount<this.Tokens.size()) {
            return (this.Tokens.get(TokenCount).token == TokenList.NUMBER || this.Tokens.get(TokenCount).token == TokenList.IDENTIFIER);
        }
        return false;
        }

    private TokenList getCurrentToken(){
        return this.Tokens.get(this.CurrentToken).token;
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



    ///////////////////////////////////////////////////////////sync
    private void Sync()throws EofException{
        do {
            this.next();//skip until a new statement is detected
        } while (!token.statements.contains(getCurrentToken()));
    }
    ///////////////////////////////////////////////////////////sync
}


