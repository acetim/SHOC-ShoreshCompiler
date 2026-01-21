
enum TokenList{//if u want to add a keyword,an operator or whatever state it before u create it.eg KEYWORD_INT ,not just INT
    KEYWORD_INT,
    KEYWORD_RETURN,
    IDENTIFIER,
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_DEVIDES,
    OPERATOR_MULTIPLIE,
    OPERATOR_EQUALS,
    OPERATOR_NOTEQUALS,
    OPERATOR_GREATERTHEN,
    OPERATOR_SMALLERTHEN,
    SEMICOLON,
    COMMENT,
    NUMBER,
    KEYWORD_IF,
    OPENING_BRACKET,
    CLOSING_BRACKET,
    OPENING_ROUND_BRACKET,//add to tokenizer
    CLOSING_ROUND_BRACKET,//add to tokenizer
    KEYWORD_WHILE//add to tokenizer

}

public class token {
    TokenList token;
    String value;

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
