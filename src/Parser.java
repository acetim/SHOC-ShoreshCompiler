import java.util.ArrayList;
import java.util.HashMap;
public class Parser {
    private final ArrayList<token> Tokens;
    private int CurrentToken;
    private HashMap<TokenList,BindingPower> BindingPowers;

    public Parser(ArrayList<token> tokens) {
        this.Tokens = tokens;
        this.CurrentToken = 0;
        //////////////////////////////////////////////MODIFY THIS AND operators IN TOKEN CLASS TO ADD/REMOVE OPERATORS IN THE PARSER
        this.BindingPowers.put(TokenList.OPERATOR_PLUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MINUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MULTIPLIE,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_DEVIDES,new BindingPower(2.0,2.1));
        //////////////////////////////////////////////MODIFY THIS AND operators IN TOKEN CLASS TO ADD/REMOVE OPERATORS IN THE PARSER
    }

    public AstStatement ParseStatement(){// returns null on comment statement !! HANDLE THIS !!
        switch(this.Tokens.get(CurrentToken).token)
        {
            case TokenList.KEYWORD_INT:
                //return this.ParseIntDecleration();
            case TokenList.KEYWORD_EXIT:
                return this.ParseExit();
            case TokenList.COMMENT:
               return this.ParseComment();

        }
    }

//    private AstStatement ParseIntDecleration(){
//
//
//    }
    private AstStatement ParseExit(){
        this.CurrentToken++;
        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        this.CurrentToken++;
        AstExpression ExitCode = ParseExpression(0.0);
        if(checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        return new AstExitStatement(ExitCode);

    }

    private AstStatement ParseIfStatement(){//throw execptions
        this.CurrentToken++;
        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        this.CurrentToken++;
        AstExpression Condition = ParseExpression(0.0);
        if(checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        this.CurrentToken++;
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock(){//should get the token that is the first { and return when encountering } and change the current pointer so it is one token after the end of the codeblock(main parser should have the same behavior)

        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_BRACKET)){
            //TODO throw syntax error
        }
        ArrayList<AstStatement> Statements = new ArrayList<AstStatement>();
        while(!checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_BRACKET)){
            Statements.add(ParseStatement());
        }
        CurrentToken++;
        return new AstCodeBlock(Statements);
    }
    //when implementing this, check all usages of parse expression for correct usage
    //TODO TEST THIS FUNC
    //currently the parser returns when the current token is a semicolon marking the end of the expression
    private AstExpression ParseExpression(Double min_bp){//should parse an expression and change the current token pointer so it is one token after the last token of the expression ,takes the first token of the expression
        if(!IsAtom(this.CurrentToken)){
            //TODO panic and exit
        }
        //create an atom
        AstExpression lhs = new AstAtomExpression(Tokens.get(this.CurrentToken).value,(getToken()==TokenList.IDENTIFIER));

        while(true){
            if(this.peek()==null) {//EOF
                //TODO throw end of file exception
            }
            if(this.peek().token==TokenList.SEMICOLON) {//if detected a semicolon - end parsing
                this.CurrentToken++;//set the count to the end of the expression
                break;
            }
            if(!isOp(this.peek().token)) {//if wrong token is detected throw error
                //TODO throw syntax error
            }

            this.next();
            TokenList op = getToken();//get operation

            //get binding powers
            Double l_bp = this.BindingPowers.get(op).getLeft();
            Double r_bp = this.BindingPowers.get(op).getRight();

            //if the current binding power of the operation is smaller than the previous, break and return lhs (no need to attach to weaker operation)
            if(l_bp<min_bp){break;}

            this.next();
            AstExpression rhs = ParseExpression(r_bp);//if not recurse and keep exploring
            lhs = new AstBinOpExpression(lhs,rhs,op);
        }
        return lhs;
    }



    /////////////////////////////////////////////////////////////helper functions
    private boolean checkIfCurrentTokenIsEqualTo(TokenList token){
        return this.Tokens.get(this.CurrentToken).token==token;
    }

    private boolean IsAtom(int TokenCount){
        if(TokenCount<this.Tokens.size()) {
            return (this.Tokens.get(TokenCount).token == TokenList.NUMBER || this.Tokens.get(TokenCount).token == TokenList.IDENTIFIER);
        }
        return false;
        }

    private TokenList getToken(){
        return this.Tokens.get(this.CurrentToken).token;
    }

    private token peek(){
        if(this.CurrentToken<this.Tokens.size()) {
            return this.Tokens.get(this.CurrentToken + 1);
        }
        return null;
    }

    private AstStatement ParseComment(){
        this.next();
        return null;
    }

    private boolean isOp(TokenList t){
        for (TokenList element : token.operators) {
            if (element == t) {
                return true;
            }
        }
        return false;
    }

    private void next(){
        this.CurrentToken++;
    }

    ///////////////////////////////////////////////////////////helper functions
}


