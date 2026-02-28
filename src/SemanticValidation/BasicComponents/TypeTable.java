package SemanticValidation.BasicComponents;

import Tokenization.TokenList;

import java.util.Map;

public class TypeTable {
    public static Map<TokenList,Integer> TypeTable=Map.ofEntries(
            Map.entry(TokenList.KEYWORD_INT,4)
            );
}
