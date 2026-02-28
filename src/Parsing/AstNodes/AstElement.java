package Parsing.AstNodes;

import SemanticValidation.BasicComponents.Visitor;

public abstract class AstElement {
    public abstract void print(String indent);
    public abstract void accept(Visitor visitor);


}
