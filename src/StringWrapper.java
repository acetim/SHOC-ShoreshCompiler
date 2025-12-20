public class StringWrapper {

    String code;

    public StringWrapper() {
        this.code = "";
    }

    public void append(String s){
        this.code+=s;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
