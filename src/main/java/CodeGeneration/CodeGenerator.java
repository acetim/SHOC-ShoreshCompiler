package CodeGeneration;

import Parsing.AstNodes.AstCodeBlock;
import SemanticValidation.SymbolTableVisitor.GlobalSymbolTable;
import SemanticValidation.SymbolTableVisitor.SymbolTable;

import java.util.HashMap;

public abstract class CodeGenerator {
    protected final boolean shabatCheck;
    protected final codePrinter codePrinter;
    protected final HashMap<String,String> StringPool;
    protected final static char q = '"';
    protected final static String indent="    ";
    protected final GlobalSymbolTable globalSymbolTable;
    protected SymbolTable currentScope;
    protected long controlFlowCounter = 0;
    public CodeGenerator(String path,HashMap<String,String> stringPool,GlobalSymbolTable globalSymbolTable,boolean shabatCheck) {
        this.codePrinter=new codePrinter(path);
        this.StringPool=stringPool;
        this.globalSymbolTable=globalSymbolTable;
        this.shabatCheck=shabatCheck;
    }
    public abstract void generateCode(AstCodeBlock root);
    abstract void printInit();
    abstract void printMain();
    abstract void printStringPool();
    abstract void printFlush();
    abstract void printShabatCheck();

}
