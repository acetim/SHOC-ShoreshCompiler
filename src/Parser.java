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
    private AstStatement ParseComment(){
        this.next();
        return null;
    }




}


