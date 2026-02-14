package Tokenization;

public enum TokenList {//if u want to add a keyword,an operator or whatever state it before u create it.eg KEYWORD_INT ,not just INT
    KEYWORD_INT,
    KEYWORD_EXIT,//exit
    IDENTIFIER,
    DECLARATION,//SPECIAL TOKEN - USED FOR AST ONLY
    OPERATOR_PLUS,
    OPERATOR_MINUS,
    OPERATOR_DEVIDES,
    OPERATOR_MULTIPLIE,
    OPERATOR_EQUALS,//not an expression op need to add ==
    OPERATOR_NOTEQUALS,
    OPERATOR_GREATERTHAN,
    OPERATOR_SMALLERTHAN,
    SEMICOLON,
    COMMENT,
    FUNCTION_DECLERATION,
    FUNCTION_CALL,
    VOID,
    NUMBER,
    KEYWORD_IF,
    OPENING_BRACKET,
    CLOSING_BRACKET,
    OPENING_ROUND_BRACKET,//add to Tokenization.tokenizer
    CLOSING_ROUND_BRACKET,//add to Tokenization.tokenizer
    KEYWORD_WHILE,//add to Tokenization.tokenizer
    PRINT_STRING,
    PRINT_INT,
    STRING_LITERAL//add to Tokenization.tokenizer

}
