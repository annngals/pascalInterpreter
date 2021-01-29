enum class TokenType{
    NUMBER,
    PLUS,
    MINUS,
    MUL,
    DIV,
    LPAREN,
    RPAREN,
    EOL,
    BEGIN,
    END,
    DOT,
    ASSIGN,
    ID
}

class Token(val type: TokenType, val value: String){

    override fun toString(): String {
        return "Token ($type, $value)"
    }
}