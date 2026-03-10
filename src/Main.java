import CodeGeneration.CodeGenerator;
import Parsing.AstNodes.AstCodeBlock;
import Parsing.Parser;
import SemanticValidation.SymbolTableVisitor.SymbolTableVisitor;
import SemanticValidation.TypeCheckerVisitor.TypeCheckerVisitor;
import Tokenization.Tokenizer;


import java.util.HashSet;
import java.util.Set;


public class Main {
    public static Set<String> knownFlags = Set.of("-no-shabat-chk");
    public static void main(String [] args) {
        ////////////////////////////////////////////////////flags
        Set<String> flags=checkArgs(args);
        String input = args[args.length-1];
        String output = args[args.length-2];
        boolean shabatCheck= !flags.contains("-no-shabat-chk");
        ////////////////////////////////////////////////////lex
        Tokenizer t = new Tokenizer(input,shabatCheck);
        t.Tokenize();
        ////////////////////////////////////////////////////parse
        Parser p = new Parser(t.getTokens());
        p.Parse();
        AstCodeBlock root = new AstCodeBlock(p.getAllStatements());
        ////////////////////////////////////////////////////semantic validation
        SymbolTableVisitor symbolTableVisitor = new SymbolTableVisitor();
        symbolTableVisitor.visit(root);
        TypeCheckerVisitor typeCheckerVisitor =new TypeCheckerVisitor(symbolTableVisitor.getGlobalSymbolTable());
        typeCheckerVisitor.visit(root);

        ////////////////////////////////////////////////////generate code

        CodeGenerator c = new CodeGenerator(output,symbolTableVisitor.getStringPool(),symbolTableVisitor.getGlobalSymbolTable(),shabatCheck);
        c.generateCode(root);
        System.out.println("\u001B[32m" + "\n\n!הקומפילציה הסתיימה בהצלחה!" + "\u001B[0m");
        System.out.println(output+"\u001B[32m" + " כתב לתוך  " + "\u001B[0m");

    }

    public static Set<String> checkArgs(String[] args){
        Set<String> flags = new HashSet<>();
        if(args.length<2){
            System.err.println("!אנא תן את הקובץ ממנו לקרוא את הקוד ואת הקובץ שאליו יש לכתוב את הקוד!");
            System.err.println("arg1->output;arg2->input");
            System.exit(1);
        }
        for(int i=0;i<args.length-2;i++){
            if(!knownFlags.contains(args[i])){
                System.err.println("דגל לא יודע:" +args[i]);
                System.exit(0);
            }
            flags.add(args[i]);
        }
        if(args[args.length-2].charAt(0)=='-' ||args[args.length-1].charAt(0)=='-'){
            System.err.println("שם של קובץ לא יכול להתחיל בקידומת של דגל");
            System.exit(0);
        }
        return flags;
    }


}