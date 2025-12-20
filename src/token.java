public class token {
    TokenList token;
    String value;

    public token(TokenList token, String value) {
        this.token = token;
        this.value = value;
    }

    public TokenList getToken() {
        return token;
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
