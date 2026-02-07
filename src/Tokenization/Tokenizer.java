package Tokenization;
import Exceptions.TokenizerException;

import java.util.ArrayList;
import java.util.Map;

public class Tokenizer {
    private final FileIO file;
    private int current;//current char being processed
    private final ArrayList<token> tokens;
    private final static Map<Character, TokenList> operators = Map.of(
            '+', TokenList.OPERATOR_PLUS,
            '-', TokenList.OPERATOR_MINUS,
            '*', TokenList.OPERATOR_MULTIPLIE,
            '/', TokenList.OPERATOR_DEVIDES,
            '=', TokenList.OPERATOR_EQUALS,
            ';', TokenList.SEMICOLON,
            '(', TokenList.OPENING_ROUND_BRACKET,
            ')', TokenList.CLOSING_ROUND_BRACKET,
            '{',TokenList.OPENING_BRACKET,
            '}',TokenList.CLOSING_BRACKET
    );
    private final static Map<String, TokenList> statements = Map.of(
            "מספר", TokenList.KEYWORD_INT,
            "ויהי_חושך", TokenList.KEYWORD_EXIT,
            "אם", TokenList.KEYWORD_IF,
            "בעוד",TokenList.KEYWORD_WHILE,
            "ויאמר",TokenList.PRINT_STRING,
            "ויאמר_מספר",TokenList.PRINT_INT
    );
    public Tokenizer(String filePath) {
        this.current=0;
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
            }
            this.file.closeIO();
        }catch (TokenizerException e){
            System.err.println("תו לא ברור זוהה");
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
                this.tokens.add(new token(String.valueOf(c),operators.get(c)));
                this.current++;
                continue;
            }
            if(Character.isDigit(c)){
                String number = TokenizeNumberLiteral(code);
                this.tokens.add(new token(number,TokenList.NUMBER));
                continue;
            }
            if(Character.isAlphabetic(c)){
                this.tokens.add(TokenizeIdentifierOrStatement(code));
                continue;
            }
            throw new TokenizerException("תו לא ברור זוהה");
        }
    }

    private String TokenizeNumberLiteral(String code){
        /*
        is called on the first char of the number literal
        sets this.current to be one token after the number literal
        returns the number as a string value
         */
        String buf="";
        while(Character.isDigit(code.charAt(current))){
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
        while(Character.isAlphabetic(code.charAt(current))||Character.isDigit(code.charAt(current))||code.charAt(current)=='_'){
            buf=buf+code.charAt(current);
            current++;
        }
        if(statements.containsKey(buf)){
            return new token(buf,statements.get(buf));
        }
        return new token(buf,TokenList.IDENTIFIER);
    }


}
