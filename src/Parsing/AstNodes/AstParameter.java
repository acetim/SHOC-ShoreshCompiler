package Parsing.AstNodes;

import Tokenization.TokenList;

public class AstParameter {
    TokenList type;
    String Name;

    public AstParameter(TokenList type, String name) {
        this.type = type;
        Name = name;
    }

    public TokenList getType() {
        return type;
    }

    public String getName() {
        return Name;
    }
}
