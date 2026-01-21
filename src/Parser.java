import java.util.ArrayList;
import java.util.HashMap;
public class Parser {
    private ArrayList<token> Tokens;
    private int CurrentToken;
    private HashMap<TokenList,BindingPower> BindingPowers;

    public Parser(ArrayList<token> tokens) {
        this.Tokens = tokens;
        this.CurrentToken = 0;
        /////////////////////////////////////////////////////////////////////////////////////////MODIFY THIS TO ADD/REMOVE OPERATORS IN THE PARSER
        this.BindingPowers.put(TokenList.OPERATOR_PLUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MINUS,new BindingPower(1.0,1.1));
        this.BindingPowers.put(TokenList.OPERATOR_MULTIPLIE,new BindingPower(2.0,2.1));
        this.BindingPowers.put(TokenList.OPERATOR_DEVIDES,new BindingPower(2.0,2.1));
        /////////////////////////////////////////////////////////////////////////////////////////MODIFY THIS TO ADD/REMOVE OPERATORS IN THE PARSER
    }
    private void next(){
        this.CurrentToken++;
    }
    public AstStatement ParseStatement(){// returns null on comment statement !! HANDLE THIS !!
        switch(this.Tokens.get(CurrentToken).token)
        {
            case TokenList.KEYWORD_INT:
                //this.ParseIntDecleration();
            case TokenList.KEYWORD_RETURN:
                //this.ParseReturn();
            case TokenList.COMMENT:
                this.ParseComment();

        }
    }

//    private AstStatement ParseIntDecleration(){
//      //TODO
//
//    }
//    private AstStatement ParseReturn(){
//        //TODO
//    }
    private AstStatement ParseIfStatement(){//throw execptions
        this.CurrentToken++;
        if(checkIfCurrentTokenIsEqualTo(TokenList.OPENING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        AstExpression Condition = ParseExpression();
        if(checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_ROUND_BRACKET)){
            //TODO throw syntax error
        }
        AstCodeBlock TrueBlock =ParseCodeBlock();

        return new AstIfStatement(Condition,TrueBlock);
    }

    private AstCodeBlock ParseCodeBlock(){//should get the token that is the first { and return when encountering } and change the current pointer so it is one token after the end of the codeblock(main parser should have the same behavior)
        ArrayList<AstStatement> Statements = new ArrayList<AstStatement>();
        while(!checkIfCurrentTokenIsEqualTo(TokenList.CLOSING_BRACKET)){
            Statements.add(ParseStatement());
        }
        CurrentToken++;
        return new AstCodeBlock(Statements);
    }
    private AstExpression ParseExpression(){//should parse an expression and change the current token pointer so it is one token after the last token of the expression
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


