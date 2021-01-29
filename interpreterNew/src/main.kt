fun main(args: Array<String>) {
    var interpreter: Interpreter
    var parser: Parser

    var test1 = "BEGIN\n" +
            "END."
    var test2 = "BEGIN\n" +
            "\tx:= 2 + 3 * (2 + 3);\n" +
            "        y:= 2 / 2 - 2 + 3 * ((1 + 1) + (1 + 1));\n" +
            "END."
    var test3 = "BEGIN\n" +
            "    y: = 2;\n" +
            "    BEGIN\n" +
            "        a := 3;\n" +
            "        a := a;\n" +
            "        b := 10 + a + 10 * y / 4;\n" +
            "        c := a - b\n" +
            "    END;\n" +
            "    x := 11;\n" +
            "END."


    parser = Parser(Lexer(test3))
    interpreter = Interpreter()
    val tree = parser.parse()
    try {
        interpreter.interpret(tree)
    } catch (e: InterpreterException) {
        System.err.println("\n$e")
    }
    println("\nDone")
}