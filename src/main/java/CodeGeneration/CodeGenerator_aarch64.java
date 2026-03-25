package CodeGeneration;

import Parsing.AstNodes.*;
import SemanticValidation.BasicComponents.Visitor;
import SemanticValidation.SymbolTableVisitor.GlobalSymbolTable;

import java.util.ArrayList;
import java.util.HashMap;
//x15 is off-limits!!!
//used for variable offsets
//todo optimize return logic to jump to end of func
public class CodeGenerator_aarch64 extends CodeGenerator implements Visitor {
    public CodeGenerator_aarch64(String path, HashMap<String, String> stringPool, GlobalSymbolTable globalSymbolTable, boolean shabatCheck) {
        super(path, stringPool, globalSymbolTable, shabatCheck);
    }
    @Override
    public void generateCode(AstCodeBlock root){
        this.printStringPool();
        this.printInit();
        root.accept(this);
        if(this.shabatCheck) {
            this.printShabatCheck();
        }
        this.printMain();
        codePrinter.write(".section .note.GNU-stack,\"\",@progbits\n");
        codePrinter.closePrinter();
    }
    @Override
    protected void printInit() {
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
    @Override
    protected void printMain() {
        String asmEntry = this.globalSymbolTable.getFunc("בראשית").getFuncName();
        codePrinter.write("""
                main:
                    stp x29, x30, [sp,#-16]
                    mov x29, sp
                """);
        if(this.shabatCheck){
            codePrinter.write("""
                        bl  SHABATCHK
                    """);
        }
        codePrinter.write(String.format("""
                    bl %s
                    mov w0, #0
                    
                    mov sp, x29
                    ldp x29, x30, [sp], #16
                    ret
                """,asmEntry));


    }
    @Override
    protected void printStringPool() {
        codePrinter.write(".section .rodata\n.balign 4\n");
        for (String key:this.StringPool.keySet()){
            codePrinter.write(indent+this.StringPool.get(key)+": .asciz "+q+key+q+'\n');
        }
        codePrinter.write(indent+"int: .asciz "+q+"%d"+q+'\n');
        codePrinter.write(indent+"shabat: .asciz "+q+"!שבת היום!\\n"+q+'\n');
        codePrinter.write("\n\n");
    }
    @Override
    protected void printFlush() {
        codePrinter.write("""
                   adrp x0, :got:stdout
                   ldr x0, [x0, :lo12:stdout]
                   ldr x0, [x0]
                   bl fflush 
                """);
    }
    @Override
    protected void printShabatCheck() {
        codePrinter.write("""
                SHABATCHK:
                    stp x29, x30, [sp,#-32]!
                    mov x29, sp
                    
                    sub sp, sp, #16
                    mov x1, sp
                    mov x8, #113
                    mov x0, #0
                    svc #0
                    ldr x0, [sp]
                    add sp, sp, #16
                    
                    ldr x1, =86400
                    udiv x0, x0, x1
                    add x0, x0, 5
                    udiv x2, x0, x1
                    msub x0, x2, x1, x0
                    
                    cmp x0, #0
                    b.ne TZADIK
                    
                    adrp x0, shabat
                    add x0, x0, :lo12:shabat
                    bl printf
                    adrp x0, :got:stdout
                    ldr x0, [x0, :lo12:stdout]
                    ldr x0, [x0]
                    bl fflush
                    mov x0, #1
                    mov x8, #93
                    svc #0                    
                    
                    TZADIK:
                    mov sp, x29
                    ldp x29, x30, [sp], #16
                    ret
                    
                """);
    }

    @Override
    public void VisitAstIfStatement(AstIfStatement node) {
        String endIf="IFEND"+this.controlFlowCounter;
        node.getCondition().accept(this);
        codePrinter.write(String.format("""
                    tst w0, #1
                    cbz w0, %s
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
        int alignedTotalOffset=((node.getTotalStackOffset()*-1)+15)& ~15;
        //make sure x29 is bp
        codePrinter.write(String.format("""
                
                %s:
                    stp x29, x30, [sp,#-16]!
                    mov x29, sp
                    sub sp, sp, #%d
                    
                """,asmFuncName,alignedTotalOffset));
        node.getBody().accept(this);
        codePrinter.write("""
          
                    mov sp, x29
                    ldp x29, x30, [sp], #16
                    ret
                
                """);
    }

    @Override
    public void VisitAstWhileStatement(AstWhileStatement node) {
        String startWhile ="WHILESTART"+this.controlFlowCounter;
        String endWhile="WHILEEND"+this.controlFlowCounter;
        codePrinter.write(indent+startWhile+":\n");
        node.getCondition().accept(this);
        codePrinter.write(String.format("""
                    tst w0, #1
                    cbz w0, %s
                """,endWhile));
        node.getCodeBlock().accept(this);
        codePrinter.write(String.format("""
                    b %s
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
                codePrinter.write(String.format("""
                            ldr w0, [x29,#%s]
                        """,this.currentScope.getOffsetStr(node.getToken().getValue())));
                return;
            }
            case NUMBER -> {
                int num = Integer.parseInt(node.getToken().getValue());
                if (num >= 0 && num <= 0xFFFF){
                    codePrinter.write(String.format("""
                                mov w0, #%s
                            """,node.getToken().getValue()));
                }else{
                    int low = num&0xFFFF;
                    int high =(num>>>16)&0xFFFF;
                    codePrinter.write(String.format("""
                                movz x0, #%d
                                movk x0, #%d, lsl #16
                            """,low,high));
                }
                return;
            }
        }
        //////////END ATOMS
        if(node.getLeft()!=null){
            /*
            if unary - left=w0
             */
            node.getLeft().accept(this);
            //load val to eax
        }
        if(node.getRight()!=null){
            /*
            if binary-
            left = w1
            right = w0
             */
            codePrinter.write("""
                        str w0, [sp, #-16]!
                    """);//save w0
            node.getRight().accept(this);//load new val to w0
            codePrinter.write("""
                        ldr w1, [sp], #16
                    """);//old val is saved in w1 and ready for binary opration
        }
        ////////////OPERATORS
        switch (node.getToken().getToken()){
            case OPERATOR_PLUS -> {
                codePrinter.write("""
                        add w0, w0, w1
                    """);
            }
            case OPERATOR_MINUS -> {
                if (node.getRight()!=null){
                    codePrinter.write("""
                                    sub w0, w1, w0
                                """);

                }
                else{
                    codePrinter.write("neg w0, w0\n");
                }

            }
            case OPERATOR_AND -> {
                codePrinter.write(indent+"and w0, w0, w1\n");
            }
            case OPERATOR_OR -> {
                codePrinter.write(indent+"orr w0, w1, w0\n");
            }
            case OPERATOR_MULTIPLIE -> {
                codePrinter.write(indent+"mul w0, w0, w1\n");
            }
            case OPERATOR_DEVIDES -> {
                codePrinter.write("""
                            sdiv w0, w1, w0
                        """);
            }
            case OPERATOR_EQUALS -> {
                codePrinter.write("""
                            cmp w0, w1
                            cset w0, eq
                        """);
            }
            case OPERATOR_GREATERTHAN -> {
                //cmp ebx than eax beacuse left->right
                codePrinter.write("""
                            cmp w0, w1
                            cset w0, gt
                        """);
            }
            case OPERATOR_SMALLERTHAN -> {
                codePrinter.write("""
                            cmp w0, w1
                            cset w0, lt
                        """);
            }
            case OPERATOR_NOT -> {
                codePrinter.write("""
                            mvn w0, w0
                        """);
            }
            default -> {
                System.err.println("unhandled operator (or token) "+node.getToken().toString()+" during code generation");
            }
        }
        ////////////END OPERATORS
    }

    @Override
    public void VisitAstExitStatement(AstExitStatement node) {
        node.getExitCode().accept(this);
        codePrinter.write("""
                    sxtw x0, w0
                    mov x8, #93
                    svc #0
                """);
    }

    @Override
    public void VisitAstFunctionCallStatement(AstFunctionCallStatement node) {
        node.getFunc().accept(this);
    }

    @Override
    public void VisitAstReturnStatement(AstReturnStatement node) {
        if (node.getReturnExpression()!=null){node.getReturnExpression().accept(this);}
        codePrinter.write("""
                    mov sp, x29
                    ldp x29, x30, [sp], #16
                    
                """);
    }

    @Override
    public void VisitAstIntDeclaration(AstIntDeclaration node) {
        if(node.getExpression()!=null){
            node.getExpression().accept(this);
            String varOffset=this.currentScope.getOffsetStr(node.getVarName());
            codePrinter.write(String.format("""
                    str w0, [x29,#%s]
                """,varOffset));
        }
    }

    @Override
    public void VisitAstFunctionExpression(AstFunctionExpression node) {
        String funcAsmName=this.globalSymbolTable.getFunc(node.getName()).getFuncName();
        ArrayList<AstExpression> args = node.getArguments();
        int totalArgs = args.size();
        int total16ByteAlingedSpace = ((totalArgs * 8) + 15) & ~15;

        codePrinter.write(String.format("""
                    sub sp, sp, #%d
                """,total16ByteAlingedSpace));

        //push args
        for (int i = 0; i < totalArgs; i++) {
            args.get(i).accept(this);
            codePrinter.write(String.format("   str w0, [sp, #%d]\n", i * 8));
        }

        codePrinter.write(String.format("""
                    bl %s
                    add sp, sp, #%d
                """,funcAsmName,total16ByteAlingedSpace));
    }

    @Override
    public void VisitAstExpressionStatement(AstExpressionStatement node) {
        node.getExpression().accept(this);
        String varOffset=this.currentScope.getOffsetStr(node.getIdentifier());
        codePrinter.write(String.format("""
                    str w0, [x29,#%s]
                """,varOffset));

    }

    @Override
    public void VisitAstInputStatement(AstInputStatement node) {
        String offset = this.currentScope.getOffsetStr(node.getIdentifier().getValue());
        String absoluteOffset = offset.substring(1);
        String op = offset.startsWith("-")? "sub":"add";

        codePrinter.write(String.format("""
                    %s x1, x29, #%s
                    adrp x0, int
                    add x0, x0, :lo12:int
                    bl scanf
                """,op,absoluteOffset));
    }

    @Override
    public void VisitAstPrintStatement(AstPrintStatement node) {
        String asmStr = this.StringPool.get(node.getStringToPrint().getValue());
        codePrinter.write(String.format("""
                    adrp x0, %s
                    add x0, x0, :lo12:%s
                    bl printf
                """,asmStr,asmStr));
        this.printFlush();

    }

    @Override
    public void VisitAstPrintIntStatement(AstPrintIntStatement node) {
        node.getExpressionToPrint().accept(this);
        codePrinter.write("""
                    sxtw x1, w0
                    adrp x0, int
                    add x0, x0, :lo12:int
                    bl printf
                """);
        this.printFlush();
    }
}
