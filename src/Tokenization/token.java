package Tokenization;


import java.util.Set;

public class token {
    //MODIFY THIS TO ADD OPERATORS TO PRATT PARSE VVVVVV
    public static Set<TokenList> logicOps=Set.of(
            TokenList.OPERATOR_GREATERTHAN,
            TokenList.OPERATOR_SMALLERTHAN,
            TokenList.OPERATOR_EQUALS,
            TokenList.OPERATOR_OR,
            TokenList.OPERATOR_AND
    );

    public static Set<TokenList> operators=Set.of(
            TokenList.OPERATOR_PLUS,
            TokenList.OPERATOR_MINUS,
            TokenList.OPERATOR_DEVIDES,
            TokenList.OPERATOR_MULTIPLIE,
            TokenList.OPERATOR_GREATERTHAN,
            TokenList.OPERATOR_SMALLERTHAN,
            TokenList.OPERATOR_EQUALS,
            TokenList.OPERATOR_OR,
            TokenList.OPERATOR_AND
    );
    //MODIFY THIS TO ADD OPERATORS TO PRATT PARSE^^^^^^
    public static Set<TokenList> statements=Set.of(
            TokenList.KEYWORD_INT,
            TokenList.KEYWORD_EXIT,
            TokenList.KEYWORD_IF,
            TokenList.KEYWORD_WHILE);
    public static Set<TokenList> types=Set.of(
            TokenList.KEYWORD_INT);

    private final TokenList token;
    private final String value;

    public token(String value,TokenList token) {
        this.value = value;
        this.token=token;
    }
    @Override
    public String toString() {
        return this.token.name()+":"+this.value;
    }
    public TokenList getToken() {
        return this.token;
    }
    public String getValue() {
        return value;
    }

}
