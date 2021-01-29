class Lexer(private val text: String) {
    private var pos: Int = 0
    private var currentChar: Char? = null
    private var temp: String = ""
    private var token: Token = Token(TokenType.BEGIN, "BEGIN")

    init {
        currentChar = text[pos]
    }

    fun nextToken(): Token? {
        var value: String
        var type: TokenType?

        while (currentChar != null) {

            if (currentChar!!.isWhitespace()) {
                skip()
                continue
            }
            if (currentChar!!.isDigit()) {
                return Token(TokenType.NUMBER, number())
            }
            if (currentChar!!.isLetter()) {
                var pair = getCharToken()
                type = pair.first
                value = pair.second

                type?.let {
                    return Token(it, value)
                }
            }

            type = null
            value = "$currentChar"
            when (currentChar) {
                '+' -> type = TokenType.PLUS
                '-' -> type = TokenType.MINUS
                '*' -> type = TokenType.MUL
                '/' -> type = TokenType.DIV
                '(' -> type = TokenType.LPAREN
                ')' -> type = TokenType.RPAREN
                ';' -> type = TokenType.EOL
                '.' -> type = TokenType.DOT
                ':' -> {
                    var nextChar = text[pos + 1]
                    forward()
                    while (nextChar != '=') {
                        nextChar = text[pos + 1]
                        forward()
                    }
                    type = TokenType.ASSIGN
                    value = ":="
                }
            }

            type?.let {
                forward()
                return Token(it, value)
            }

            throw InterpreterException("invalid token $token")
        }
        return null
    }

    private fun getCharToken(): Pair<TokenType?, String> {

        temp = ""

        while (currentChar!!.isLetter()) {
            temp += currentChar
            forward()
        }

        return when (temp) {
            "BEGIN" -> {
                Pair(TokenType.BEGIN, "BEGIN")
            }
            "END" -> {
                Pair(TokenType.END, "END")
            }
            else -> Pair(TokenType.ID, temp)
        }
    }

    private fun forward() {
        pos += 1
        if (pos > text.length - 1) {
            currentChar = null
        } else { currentChar = text[pos] }
    }

    private fun skip() {
        while ((currentChar != null) && currentChar!!.isWhitespace()) {
            forward()
        }
    }

    private fun number(): String {
        var result = arrayListOf<Char>()
        while ((currentChar != null) && (currentChar!!.isDigit() || currentChar == '.')) {
            result.add(currentChar!!)
            forward()
        }
        return result.joinToString("")
    }
}

fun main(args: Array<String>) {
    val lexer = Lexer(
        "BEGIN\n" +
                "    y: = 2;\n" +
                "    BEGIN\n" +
                "        a := 3;\n" +
                "        a := a;\n" +
                "        b := 10 + a + 10 * y / 4;\n" +
                "        c := a - b\n" +
                "    END;\n" +
                "    x := 11;\n" +
                "END."
    )
    var token = lexer.nextToken()
    while (token != null) {
        println(token)
        token = lexer.nextToken()
    }
}