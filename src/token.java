public class token {
    tokenList token;
    String value;

    public token(tokenList token, String value) {
        this.token = token;
        this.value = value;
    }

    public tokenList getToken() {
        return token;
    }

    public void setToken(tokenList token) {
        this.token = token;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
