import Parsing.AstNodes.AstCodeBlock;
import Parsing.AstNodes.PARSER_TESTING;
import Parsing.Parser;
import SemanticValidation.SymbolTableVisitor.SymbolTableVisitor;
import Tokenization.Tokenizer;

public class Main {
    public static void main(String [] args) {
        Tokenizer t = new Tokenizer("code.txt");
        t.Tokenize();

        Parser p = new Parser(t.getTokens());
        p.Parse();

        AstCodeBlock root = new AstCodeBlock(p.getAllStatements());
        SymbolTableVisitor symbolTableVisitor=new SymbolTableVisitor();
        symbolTableVisitor.visit(root);

        PARSER_TESTING.printElements("",p.getAllStatements());
    }
}