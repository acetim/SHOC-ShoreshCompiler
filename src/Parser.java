import java.util.ArrayList;

public class Parser {
    private ArrayList<token> Tokens;
    private int CurrentToken;

    public Parser(ArrayList<token> tokens) {
        this.Tokens = tokens;
        this.CurrentToken = 0;
    }
    private void next(){
        this.CurrentToken++;
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
        AstExpression ExitCode = ParseExpression();
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
        AstExpression Condition = ParseExpression();
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


    //when implementing this, check all usages of parseexpression for correct usage
    private AstExpression ParseExpression(){//should parse an expression and change the current token pointer so it is one token after the last token of the expression ,takes the first token of the expression
        //TODO implement
    }
    private AstStatement ParseComment(){
        this.next();
        return null;
    }


    /////////////////////////////////////////////////////////////helper functions
    private boolean checkIfCurrentTokenIsEqualTo(TokenList token){
        return this.Tokens.get(this.CurrentToken).token==token;
    }


}


