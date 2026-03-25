package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstParameter extends AstStatement{
    TokenList type;
    String Name;

    public AstParameter(TokenList type, String name) {
        super(TokenList.IDENTIFIER);
        this.type = type;
        Name = name;
    }

    public TokenList getType() {
        return type;
    }

    public String getName() {
        return Name;
    }

    @Override
    public void print(String indent) {
        System.out.print(this.type.name()+" "+this.Name+";");
    }
}
