class InterpreterException(message: String) : Exception(message)

class Interpreter() : NodeVisitor {

    private var variableList: VariableList = VariableList()

    private fun visitNumber(node: Node): Double {
        val number = node as Number

        return number.token.value.toDouble()
    }

    private fun visitBinOp(node: Node): Double {
        val operator = node as BinOp

        when (operator.op.type) {
            TokenType.PLUS -> return visit(operator.left) as Double + visit(operator.right) as Double
            TokenType.MINUS -> return visit(operator.left) as Double - visit(operator.right) as Double
            TokenType.MUL -> return visit(operator.left) as Double * visit(operator.right) as Double
            TokenType.DIV -> return visit(operator.left) as Double / visit(operator.right) as Double
        }
        throw  InterpreterException("invalid BinOp $operator")
    }

    private fun visitUnaryOp(node: Node): Double {
        val operator = node as UnaryOp

        when (operator.op.type) {
            TokenType.PLUS -> return +(visit(operator.expr) as Double)
            TokenType.MINUS -> return -(visit(operator.expr) as Double)
        }
        throw  InterpreterException("invalid UnaryOp $operator")
    }

    private fun visitVariable(node: Node): Double? { //
        val variable = node as Variable
        return variableList[variable.token.value]
    }

    private fun visitCover(node: Cover): Any? {
        for (expression in node.expressions)
            visit(expression)
        return null
    }

    private fun visitAssign(node: Node): Any? {
        val assigner: AssignOp = node as AssignOp
        val value = visit(assigner.node)
        variableList[assigner.variable.token.value] = value as Double
        return null
    }

    override fun visit(node: Node): Any? {
        when (node) {
            is Number -> return visitNumber(node)
            is BinOp -> return visitBinOp(node)
            is UnaryOp -> return visitUnaryOp(node)
            is Variable -> return visitVariable(node)
            is Cover -> return visitCover(node)
            is AssignOp -> return visitAssign(node)
            is NoOp -> {
                println(variableList)
                return null
            }
        }
        throw InterpreterException("invalid node")
    }

    fun interpret(tree: Node) {
        visit(tree)
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
    val tree = parser.parse()
    val interpreter = Interpreter()
    interpreter.interpret(tree)
}