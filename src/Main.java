import Parsing.AstNodes.PARSER_TESTING;
import Parsing.Parser;
import Tokenization.Tokenizer;
import Tokenization.token;

public class Main {
    public static void main(String [] args) {
        Tokenizer t = new Tokenizer("code.txt");
        t.Tokenize();
        Parser p = new Parser(t.getTokens());
        p.Parse();
        PARSER_TESTING.printElements("",p.getAllStatements());
    }
}