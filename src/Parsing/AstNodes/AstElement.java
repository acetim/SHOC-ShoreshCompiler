package Parsing.AstNodes;

import SemanticValidation.Visitor;

public abstract class AstElement {
    public abstract void print(String indent);
    public abstract void accept(Visitor visitor);


}
