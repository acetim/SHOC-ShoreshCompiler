package Parsing.AstNodes;

import java.util.ArrayList;

public class PARSER_TESTING {
    public static void printElements(String indent, ArrayList<AstStatement> elements){
        if(elements!=null) {
            for (AstElement e : elements) {
                e.print(indent);
            }
        }
    }
    public static String getPrefixString(AstExpression node) {
        if (node == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(node);

        if (node.getLeft() != null || node.getRight() != null) {
            sb.append("(");
            sb.append(getPrefixString(node.getLeft()));
            sb.append(",");
            sb.append(getPrefixString(node.getRight()));
            sb.append(")");
        }

        return sb.toString();
    }



}
