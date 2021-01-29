import kotlin.collections.ArrayList

class ParserException(message: String) : Exception(message)

class Parser(private val lexer: Lexer) {

    private var currentToken: Token? = lexer.nextToken()
    private var nextToken: Token? = lexer.nextToken()
    private lateinit var currentVar: Variable

    private fun checkTokenType(type: TokenType) {
        if (currentToken!!.type == type) {
            currentToken = nextToken
            nextToken = lexer.nextToken()
        } else {
            throw ParserException("invalid token order")
        }
    }

    private fun factor(): Node {
        var token = currentToken

        when (token!!.type) {
            TokenType.PLUS -> {
                checkTokenType(TokenType.PLUS)
                return UnaryOp(token, factor())
            }
            TokenType.MINUS -> {
                checkTokenType(TokenType.MINUS)
                return UnaryOp(token, factor())
            }
            TokenType.NUMBER -> {
                checkTokenType(TokenType.NUMBER)
                return Number(token)
            }
            TokenType.LPAREN -> {
                checkTokenType(TokenType.LPAREN)
                val result = expr()
                checkTokenType(TokenType.RPAREN)
                return result
            }
            TokenType.ID -> return variable()
        }
        throw ParserException("invalid factor, $currentToken $token")
    }

    private fun term(): Node {
        val ops = arrayListOf(TokenType.DIV, TokenType.MUL)
        var result = factor()

        while (ops.contains(currentToken!!.type)) {
            val token = currentToken

            when (token!!.type) {
                TokenType.DIV -> checkTokenType(TokenType.DIV)
                TokenType.MUL -> checkTokenType(TokenType.MUL)
            }
            result = BinOp(result, token!!, factor())
        }
        return result
    }

    fun expr(): Node {
        val ops = arrayListOf(TokenType.PLUS, TokenType.MINUS)
        var result = term()

        while (ops.contains(currentToken!!.type)) {
            val token = currentToken!!

            when (token!!.type) {
                TokenType.PLUS -> checkTokenType(TokenType.PLUS)
                TokenType.MINUS -> checkTokenType(TokenType.MINUS)
            }

            result = BinOp(result, token, term())
        }
        return result
    }

    private fun empty(): Node{
        return NoOp()
    }

    private fun variable(): Node {
        currentVar = Variable(currentToken!!)
        checkTokenType(TokenType.ID)
        return currentVar
    }

    private fun assignment(): Node {
        val left = variable() as Variable
        checkTokenType(TokenType.ASSIGN)
        val right = expr()
        return AssignOp(left, right)
    }

    private fun statement(): Node {
        return when (currentToken!!.type) {
            TokenType.BEGIN -> complexStatement()
            TokenType.ID -> assignment()
            else -> empty()
        }
    }

    private fun statementList(): ArrayList<Node> {
        val node = statement()
        val result = arrayListOf(node)
        while (currentToken!!.type == TokenType.EOL) {
            checkTokenType(TokenType.EOL)
            result.add(statement())
        }
        return result
    }

    private fun complexStatement(): Node {
        checkTokenType(TokenType.BEGIN)
        val nodes = statementList()
        checkTokenType(TokenType.END)
        return Cover(nodes)
    }

    fun parse(): Node {
        val node = complexStatement()
        checkTokenType(TokenType.DOT)
        return node
    }
}

fun main(args: Array<String>) {
    val parser = Parser(
        Lexer(
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
    )
    print(parser.parse())
}