package CodeGeneration;

import Parsing.AstNodes.*;
import SemanticValidation.BasicComponents.Visitor;
import SemanticValidation.SymbolTableVisitor.GlobalSymbolTable;
import SemanticValidation.SymbolTableVisitor.SymbolTable;

import java.util.HashMap;

public class CodeGenerator implements Visitor {
    private final codePrinter codePrinter;
    private final HashMap<String,String> StringPool;
    private final static char q = '"';
    private final static String indent="    ";
    private final GlobalSymbolTable globalSymbolTable;
    private SymbolTable currentScope;
    private long controlFlowCounter = 0;
    public CodeGenerator(String path,HashMap<String,String> stringPool,GlobalSymbolTable globalSymbolTable) {
        this.codePrinter=new codePrinter(path);
        this.StringPool=stringPool;
        this.globalSymbolTable=globalSymbolTable;
    }

    public void generateCode(AstCodeBlock root){
        codePrinter.write(".intel_syntax noprefix\n\n");
        this.printStringPool();
        this.printInit();
        root.accept(this);
        this.printMain();
        codePrinter.closePrinter();
    }
    private void printInit(){
        codePrinter.write(
                """
                        .section .text
                        
                        .global main
                        .extern printf
                        .extern scanf
                        .extern stdout
                        .extern fflush
                        
                        """
        );
    }
    private void printMain(){
        codePrinter.write(
                    "main:\n"
                    +indent+"push rbp\n"
                    +indent+"mov rbp,rsp\n\n"

                    +indent+"call "+this.globalSymbolTable.getFunc("בראשית").getFuncName()+"\n\n"
                    +indent+"mov eax,0\n"

                    +indent+"mov rsp,rbp\n"
                    +indent+"pop rbp\n"
                    +indent+"ret\n"


        );
    }
    private void printStringPool(){
        codePrinter.write(".section .rodata\n");
        for (String key:this.StringPool.keySet()){
            codePrinter.write(indent+this.StringPool.get(key)+": .asciz "+q+key+q+'\n');
        }
        codePrinter.write(indent+"int: .asciz "+q+"%d"+q);
        codePrinter.write("\n\n");
    }
    private void printFlush(){
        codePrinter.write("""
                    mov rdi,[rip+stdout]
                    call fflush
                """);
    }

    @Override
    public void VisitAstIfStatement(AstIfStatement node) {
        String endIf="IFEND"+this.controlFlowCounter;
        node.getCondition().accept(this);
        codePrinter.write(String.format("""
                    test eax,1
                    jz %s
                """,endIf));
        node.getTrueBlock().accept(this);
        codePrinter.write(String.format("""
                    %s:
                """,endIf));
        this.controlFlowCounter++;
    }

    @Override
    public void VisitAstFunctionDeclaration(AstFunctionDeclaration node) {
        String asmFuncName = this.globalSymbolTable.getFunc(node.getName()).getFuncName();
        codePrinter.write(
                "\n"+asmFuncName+":\n"
                +indent+"push rbp\n"
                +indent+"mov rbp,rsp\n"
                +indent+"add rsp,"+node.getTotalStackOffset()+"\n\n");

        node.getBody().accept(this);

        codePrinter.write(
                '\n'+indent+"mov rsp,rbp\n"
                +indent+"pop rbp\n"
                +indent+"ret\n");


    }

    @Override
    public void VisitAstWhileStatement(AstWhileStatement node) {
        String startWhile ="WHILESTART"+this.controlFlowCounter;
        String endWhile="WHILEEND"+this.controlFlowCounter;
        codePrinter.write(startWhile+":\n");
        node.getCondition().accept(this);
        codePrinter.write(String.format("""
                    test eax,1
                    jz %s
                """,endWhile));
        node.getCodeBlock().accept(this);
        codePrinter.write(String.format("""
                    jmp %s
                    %s:
                """,startWhile,endWhile));
        this.controlFlowCounter++;
    }

    @Override
    public void VisitAstCodeBlock(AstCodeBlock node) {
        this.currentScope=node.getScope();
        for (AstStatement statement:node.getStatements()){
            statement.accept(this);
        }
        this.currentScope=node.getScope().getParent();
    }

    @Override
    public void VisitAstExpression(AstExpression node) {
        //////////ATOMS
        switch (node.getToken().getToken()){
            case IDENTIFIER -> {
                String offset = this.currentScope.getOffsetStr(node.getToken().getValue());
                codePrinter.write(indent+"mov eax,dword ptr[rbp"+offset+"]\n");//ASSUMING ALL INT TYPES
                return;
            }
            case NUMBER -> {
                codePrinter.write(indent+"mov eax,"+node.getToken().getValue()+"\n");
                return;
            }
        }
        //////////END ATOMS
        if(node.getLeft()!=null){
            /*
            if unary - left=eax
             */
            node.getLeft().accept(this);
            //load val to eax
        }
        if(node.getRight()!=null){
            /*
            if binary-
            left = ebx
            right = eax
             */
            codePrinter.write(indent+"push rax\n");//save eax
            node.getRight().accept(this);//load new val to eax
            codePrinter.write(indent+"pop rbx\n");//old val is saved in ebx and ready for binary opration
        }
        ////////////OPERATORS
        switch (node.getToken().getToken()){
            case OPERATOR_PLUS -> {
                codePrinter.write(indent+"add eax,ebx\n");
            }
            case OPERATOR_MINUS -> {
                if (node.getRight()!=null){
                    codePrinter.write("""
                                    sub ebx,eax
                                    mov eax,ebx
                                """);

                }
                else{
                    codePrinter.write("neg eax\n");
                }

            }
            case OPERATOR_AND -> {
                codePrinter.write(indent+"and eax,ebx\n");
            }
            case OPERATOR_OR -> {
                codePrinter.write(indent+"or eax,ebx\n");
            }
            case OPERATOR_MULTIPLIE -> {
                codePrinter.write(indent+"imul eax,ebx\n");
            }
            case OPERATOR_DEVIDES -> {
                //flip eax to be left and ebx to be right
                //then divide
                codePrinter.write("""
                            xchg eax,ebx
                            idiv ebx
                        """);
            }
            case OPERATOR_EQUALS -> {
                codePrinter.write("""
                            cmp eax,ebx
                            sete al
                            movzx eax,al
                        """);
            }
            case OPERATOR_GREATERTHAN -> {
                //cmp ebx than eax beacuse left->right
                codePrinter.write("""
                            cmp ebx,eax
                            setg al
                            movzx eax,al
                        """);
            }
            case OPERATOR_SMALLERTHAN -> {
                codePrinter.write("""
                            cmp ebx,eax
                            setl al
                            movzx eax,al
                        """);
            }
            case OPERATOR_NOT -> {
                codePrinter.write(indent+"not eax\n");
            }
            default -> {
                System.err.println("unhandled operator (or token) "+node.getToken().getToken().name()+" during code generation");
            }
        }
        ////////////END OPERATORS
        


    }

    @Override
    public void VisitAstExitStatement(AstExitStatement node) {
        node.getExitCode().accept(this);
        codePrinter.write("""
                    movsx rdi,eax
                    mov rax,60
                    syscall
                """);

    }

    @Override
    public void VisitAstFunctionCallStatement(AstFunctionCallStatement node) {
        node.getFunc().accept(this);
    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {//TODO
        node.getReturnExpression().accept(this);
        codePrinter.write("""
                    movsx rax,eax
                    mov rsp,rbp
                    pop rbp
                    ret
                """);
    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {
        if(node.getExpression()!=null){
            node.getExpression().accept(this);
            String varOffset=this.currentScope.getOffsetStr(node.getVarName());
            codePrinter.write(String.format("""
                    mov dword ptr[rbp%s],eax
                """,varOffset));
        }
    }

    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node) {
        String funcAsmName=this.globalSymbolTable.getFunc(node.getName()).getFuncName();
        int stackCounter=0;
        //push last arg first
        for (AstExpression arg:node.getArguments().reversed()){
            arg.accept(this);//load result to eax
            codePrinter.write(indent+"push rax\n");
            stackCounter+=8;//count how much the stack has grown
        }
        codePrinter.write(
                indent+"call "+funcAsmName+'\n'
                 +indent+"add rsp,"+stackCounter+'\n'//stack cleanup
        );

    }

    @Override
    public void VisitAstExpressionStatement(AstExpressionStatement node) {
        node.getExpression().accept(this);
        String varOffset=this.currentScope.getOffsetStr(node.getIdentifier());
        codePrinter.write(String.format("""
                    mov dword ptr[rbp%s],eax
                """,varOffset));
    }

    @Override
    public void VisitAstInputStatement(AstInputStatement node) {
        String varOffset=this.currentScope.getOffsetStr(node.getIdentifier().getValue());
        codePrinter.write(String.format("""
                    xor eax,eax
                    lea rdi,[rip+int]
                    lea rsi,[rbp%s]
                    call scanf
                """,varOffset));
    }

    @Override
    public void VisitAstPrintStatement(AstPrintStatement node) {
        String asmStr = this.StringPool.get(node.getStringToPrint().getValue());
        codePrinter.write(
                indent+"lea rdi,[rip+"+asmStr+"]\n"
                +indent+"xor eax,eax\n"
                +indent+"call printf\n"
        );
        printFlush();
    }

    @Override
    public void VisitAstPrintIntStatement(AstPrintIntStatement node) {
        node.getExpressionToPrint().accept(this);
        codePrinter.write("""
                    movsx rsi,eax
                    lea rdi,[rip+int]
                    xor eax,eax
                
                    call printf
                
                """);
        printFlush();
    }
}
