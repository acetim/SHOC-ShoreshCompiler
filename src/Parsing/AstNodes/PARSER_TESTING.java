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
        sb.append(node.toString());

        if (node.getLeft() != null || node.getRight() != null) {
            sb.append("(");
            sb.append(getPrefixString(node.getLeft()));
            sb.append(",");
            sb.append(getPrefixString(node.getRight()));
            sb.append(")");
        }

        return sb.toString();
    }

//    public static void TEST(){
//        Map<String, String> testCases = new LinkedHashMap<>();
//        testCases.put("a+b*c;", "+(a,*(b,c))");
//        testCases.put("a*b+c;", "+(*(a,b),c)");
//        testCases.put("1-2-3;", "-(-(1,2),3)");
//        testCases.put("(a+b)*c;", "*(+(a,b),c)");
//        testCases.put("1*2+3/4;", "+(*(1,2),/(3,4))");
//        testCases.put("1*(2+3*(4-5));", "*(1,+(2,*(3,-(4,5))))");
//        testCases.put("1+2-3-4;", "-(-(+(1,2),3),4)");
//        testCases.put("(1-2/(3+4))/5;", "/(-(1,/(2,+(3,4))),5)");
//        testCases.put("(1+2)*(3+4)*(5+6);", "*(*(+(1,2),+(3,4)),+(5,6))");
//        testCases.put("1+(2+(3+(4+5)));", "+(1,+(2,+(3,+(4,5))))");
//        testCases.put("1*2-3*4+5/6;", "+(-(*(1,2),*(3,4)),/(5,6))");
//
//
//        Tokenizer t = new Tokenizer("code.txt");
//
//        for(String key :testCases.keySet()){
//            t.TEST_TOKENIZELINE(key);
//            Parser p = new Parser(t.getTokens());
//            String s = getPrefixString(p.TEST___PARSE___EXPRESSION());
//            if(s.equals(testCases.get(key))){
//                System.out.println("\u001B[32m" + "TEST PASSED" + "\u001B[0m");
//
//            }else{
//                System.out.println("TEST FAILED");
//            }
//            t.TEST_RESET();
//        }
//
//    }

}
