package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstVarDeclaration extends AstStatement{
    private String varName;
    private int sizeInBytes;

    public AstVarDeclaration(String varName, int sizeInBytes) {
        super(TokenList.DECLARATION);
        this.varName = varName;
        this.sizeInBytes = sizeInBytes;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }
}
