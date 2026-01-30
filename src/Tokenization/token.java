package Tokenization;

import java.util.Set;

public class token {
    //optimize this
    public static Set<TokenList> operators=Set.of(TokenList.OPERATOR_PLUS, TokenList.OPERATOR_MINUS, TokenList.OPERATOR_DEVIDES, TokenList.OPERATOR_MULTIPLIE,TokenList.OPERATOR_NOTEQUALS, TokenList.OPERATOR_GREATERTHEN, TokenList.OPERATOR_SMALLERTHEN);
    public static Set<TokenList> statements=Set.of(TokenList.KEYWORD_INT,TokenList.KEYWORD_EXIT,TokenList.KEYWORD_IF,TokenList.KEYWORD_WHILE);

    private TokenList token;
    private String value;

    public token(String value,TokenList token) {
        this.value = value;
        this.token=token;
    }


public TokenList getToken() {
        return this.token;
    }

    public void setToken(TokenList token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
