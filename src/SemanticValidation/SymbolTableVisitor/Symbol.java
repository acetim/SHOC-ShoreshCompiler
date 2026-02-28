package SemanticValidation.SymbolTableVisitor;

import Tokenization.TokenList;

public class Symbol {
    private final String name;
    private final int offset;
    private final TokenList type;

    public Symbol(String name, int offset, TokenList type) {
        this.name = name;
        this.offset = offset;
        this.type = type;
    }

    public String getName() {
        return name;
    }
}
