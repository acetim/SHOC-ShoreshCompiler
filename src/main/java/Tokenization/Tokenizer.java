package Tokenization;
import Exceptions.TokenizerException;

import java.util.ArrayList;
import java.util.Map;
/*
Tokenizer is o(n) time complexity where n is the number of chars in the file

the tokenizer class takes a file path for its constructor and creates a tokenizer ready to tokenize file
the class uses Tokenize() function to tokenize the file and store the tokens as an array list in the class
 */
public class Tokenizer {
    private final FileIO file;
    private int current;//current char being processed
    private final ArrayList<token> tokens;
    private int lineCount;

    private final static Map<Character, TokenList> operators = Map.ofEntries(
            Map.entry('+', TokenList.OPERATOR_PLUS),
            Map.entry('-', TokenList.OPERATOR_MINUS),
            Map.entry('*', TokenList.OPERATOR_MULTIPLIE),
            Map.entry('/', TokenList.OPERATOR_DEVIDES),
            Map.entry('=', TokenList.OPERATOR_EQUALS),
            Map.entry(';', TokenList.SEMICOLON),
            Map.entry('(', TokenList.OPENING_ROUND_BRACKET),
            Map.entry(')', TokenList.CLOSING_ROUND_BRACKET),
            Map.entry('<', TokenList.OPERATOR_SMALLERTHAN),
            Map.entry('>', TokenList.OPERATOR_GREATERTHAN)
    );
    private final static Map<String, TokenList> statements = Map.ofEntries(
            Map.entry("לא", TokenList.OPERATOR_NOT),
            Map.entry("שלם", TokenList.KEYWORD_INT),
            Map.entry("ויהי_חושך", TokenList.KEYWORD_EXIT),
            Map.entry("אם_יהיה", TokenList.KEYWORD_IF),
            Map.entry("בעוד", TokenList.KEYWORD_WHILE),
            Map.entry("ויאמר", TokenList.PRINT_STRING),
            Map.entry("ויאמר_שלם", TokenList.PRINT_INT),
            Map.entry("ויעש", TokenList.OPENING_BRACKET),
            Map.entry("ויתם", TokenList.CLOSING_BRACKET),
            Map.entry("ויברא", TokenList.FUNCTION_DECLERATION),
            Map.entry("תהו_ובהו", TokenList.VOID),
            Map.entry("ויקרא",TokenList.FUNCTION_CALL),
            Map.entry("אמן",TokenList.KEYWORD_RETURN),
            Map.entry("בסד",TokenList.BASAD),
            Map.entry("וגם",TokenList.OPERATOR_AND),
            Map.entry("או",TokenList.OPERATOR_OR),
            Map.entry("שמע",TokenList.INPUT)
    );
    public Tokenizer(String filePath) {
        this.current=0;
        this.lineCount=0;
        this.file = new FileIO(filePath);//fileIO constructor exits program if file not found
        this.tokens=new ArrayList<>();
    }

    public void Tokenize(){
        /*
        tokenizes the text in the file provided at construction
        resets the current var after each line (it points to the current char being processed)
        calls TokenizeLine() and handles exceptions
         */
        try{
            String line;
            while((line=this.file.nextLine())!=null){
                this.current=0;
                TokenizeLine(line);
                this.lineCount++;
            }
            this.file.closeIO();
            System.out.println("\u001B[32m" + "טוקניזציה הסתיימה בהצלחה" + "\u001B[0m");
        }catch (TokenizerException e){
            System.err.println(e.getMessage());
            this.file.closeIO();
            System.exit(1);
        }
    }

    private void TokenizeLine(String code) throws TokenizerException {
        /*
        all tokenizer function should set this.current as the char after the last char of the token
        gets a line of string code and adds the according tokens to the arraylist
        if an unidentified char is detected it throws a TokenizerException
         */
        while(this.current<code.length()) {
            char c = code.charAt(current);

            if(Character.isWhitespace(c)){
                this.current++;
                continue;
            }
            if(operators.containsKey(c)){
                this.tokens.add(new token(String.valueOf(c),operators.get(c),this.lineCount));
                this.current++;
                continue;
            }
            if(Character.isDigit(c)){
                String number = TokenizeNumberLiteral(code);
                this.tokens.add(new token(number,TokenList.NUMBER,this.lineCount));
                continue;
            }
            if(Character.isAlphabetic(c)){
                this.tokens.add(TokenizeIdentifierOrStatement(code));
                continue;
            }
            if(c=='"'){
                String StringLiteral=TokenizeStringLiteral(code);
                this.tokens.add(new token(StringLiteral,TokenList.STRING_LITERAL,this.lineCount));
                continue;
            }
            throw new TokenizerException(c+" :תו לא ברור זוהה");
        }
    }

    private String TokenizeStringLiteral(String code){
        this.current++;
        String buf = "";

        while(this.current<code.length()&&code.charAt(current)!='"'){
            buf+=code.charAt(current);
            this.current++;
        }
        this.current++;
        return buf;
    }

    private String TokenizeNumberLiteral(String code){
        /*
        is called on the first char of the number literal
        sets this.current to be one token after the number literal
        returns the number as a string value
         */
        String buf="";
        while(this.current<code.length()&&Character.isDigit(code.charAt(current))){
            buf=buf+code.charAt(current);
            current++;
        }
        return buf;
    }

    private token TokenizeIdentifierOrStatement(String code){
        /*
        is called on the first char of the valid letter for statement or identifier
        sets this.current to be one token after the statement or identifier
        returns the TOKEN
        valid chars for identifiers and vars are alphabetical letters ,numbers(not at the start) and underscores(not at the start)
         */
        String buf="";
        while(this.current<code.length()&&(Character.isAlphabetic(code.charAt(current))||Character.isDigit(code.charAt(current))||code.charAt(current)=='_')){
            buf=buf+code.charAt(current);
            current++;
        }
        if(statements.containsKey(buf)){
            return new token(buf,statements.get(buf),this.lineCount);
        }
        return new token(buf,TokenList.IDENTIFIER,this.lineCount);
    }

    public ArrayList<token> getTokens() {
        return tokens;
    }

}
