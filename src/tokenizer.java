import java.util.ArrayList;

import java.util.HashMap;
import java.util.Locale;//HUH????
import java.util.Map;

public class tokenizer {//TODO ADD VALUES TO TOKENS THAT ARE RETURNED
    String code;

    token current_token;

    ArrayList<TokenList> keyWords=new ArrayList<>();

    public tokenizer(String code){//constructor
        this.code=code;
        for(int i=0;i<TokenList.values().length;i++){
            if(TokenList.values()[i].name().contains("KEYWORD")) {//add only keyWords
                this.keyWords.add(TokenList.values()[i]);
            }
        }
    }



    public Object[] next(int index){//returns the new index and the token

        String buffer="";
        Object[] ret=new Object[2];

        while(Character.isWhitespace(this.code.charAt(index))){
            index++;
        }

        if(this.code.charAt(index)=='+'){
            ret[0]=new token(null,TokenList.OPERATOR_PLUS);
            ret[1]=index+1;
            return ret;
        }

        if(this.code.charAt(index)=='-'){
            ret[0]=new token(null,TokenList.OPERATOR_MINUS);
            ret[1]=index+1;
            return ret;
        }

        if(this.code.charAt(index)=='='){

            ret[0]=new token(null,TokenList.OPERATOR_EQUALS);
            ret[1]=index+1;
            return ret;

        }
        if(this.code.charAt(index)=='/'){
            ret[0]=new token(null,TokenList.OPERATOR_EQUALS);
            ret[1]=index+1;
            return ret;
        }

        if(this.code.charAt(index)=='*'){
            ret[0]=new token(null,TokenList.OPERATOR_MULTIPLIE);
            ret[1]=index+1;
            return ret;
        }
        if(this.code.charAt(index)==';'){

            ret[0]=new token(null,TokenList.SEMICOLON);
            ret[1]=index+1;
            return ret;

        }

        if(this.code.charAt(index)=='('){
            ret[0]=new token(null,TokenList.OPENING_BRACKET);
            ret[1]=index+1;
            return ret;
        }
        if(this.code.charAt(index)==')'){
            ret[0]=new token(null,TokenList.CLOSING_BRACKET);
            ret[1]=index+1;
            return ret;
        }

        boolean isDigit=false;//only used for the next while loop-not important

        while(Character.isDigit(this.code.charAt(index))||this.code.charAt(index)=='.'){//if it starts with a digit then its an int literal(or a decimal,ill treat both the same for now)
            buffer+=this.code.charAt(index);
            index++;
            isDigit=true;
        }

        if(isDigit){

            ret[0]=new token(buffer,TokenList.NUMBER);
            ret[1]=index;
            return ret;

        }

        //if the number ended with anything that is not a +,-,*,/,=,<,>,a digit then throw an error

        if(this.code.charAt(index)!='+'||this.code.charAt(index)!='-'||this.code.charAt(index)!='*'||this.code.charAt(index)!='/'||this.code.charAt(index)!='='||this.code.charAt(index)!='<'||this.code.charAt(index)!='>'){
            //throw an error-DO LATER
        }


        while(Character.isLetter(this.code.charAt(index))||Character.isDigit(this.code.charAt(index))){//in case its a keyWord/an identifier-while its a char or a number(the only characters allowed for an identifier)
            buffer+=this.code.charAt(index);
            index++;
        }
        //i also need to create a function to check if the identifier ends legaly
        TokenList keyWord=checkIfKeyWord(buffer);
        if(keyWord!=null){
            ret[0]=new token(null,keyWord);
            ret[1]=index;
            return ret;
        }
        else{
            ret[0]=new token(buffer,TokenList.IDENTIFIER);
            ret[1]=index;
            return ret;
        }

        //NOT FINISHED YET
    }

    public ArrayList<token> tokenize(){
        Character c;
        ArrayList<token> list=new ArrayList<>();
        token t;
        Object[] arr=null;
        for(int i=0;i < this.code.length();) {//dont increment i,cus next() does that for u
            arr=next(i);
            list.add((token)arr[0]);
            i=(int)arr[1];
        }

        return list;
    }
//TODO WHAT THE FUCKKK SNIR FIX THIS SHIT, YOU NEED TO MAKE A FUNC THAT TAKES THE AND MANUALY ASSIGNS A TOKEN FITTING THE KEYWORD
    private TokenList checkIfKeyWord(String word){//checks if a given word is a keyWord,and returns the keyWord in case it is
        String newWord=word.toUpperCase();//enum strings are built of capitalized letters

        for(int i=0;i<TokenList.values().length;i++){
            if(TokenList.values()[i].name().equals("KEYWORD_"+newWord)){
                return TokenList.values()[i];
            }
        }
        return null;
    }

    private boolean checkForWord(String word,int index){//checks if a given word is at a given index
        String text="";
        for(int i=0;i<word.length();i++){
            text+=this.code.charAt(i+index);
        }
        if(word.equals(text)){
            return true;
        }
        else{
            return false;
        }
    }

    public String toString(){
        ArrayList<token> lst=this.tokenize();
        String text="";
        for(int i=0;i< lst.size();i++){
            text+= lst.get(i).token.name()+" ";
        }
        return text;
    }

}


