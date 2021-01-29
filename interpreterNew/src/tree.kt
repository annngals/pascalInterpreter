import java.util.ArrayList

abstract class Node

interface NodeVisitor {
    fun visit(node: Node): Any?
}

//           Entities

class Number(val token: Token) : Node() {
    override fun toString(): String {
        return "Number ($token)"
    }
}

class Variable(var token: Token) : Node() {
    override fun toString(): String {
        return "Variable ($token)"
    }
}

class NoOp : Node() {
    override fun toString(): String {
        return "NoOp ()"
    }
}

//            Operators

class AssignOp(var variable: Variable, var node: Node) : Node() {
    override fun toString(): String {
        return "Assigner (\n$variable, $node)"
    }
}

class UnaryOp(val op: Token, val expr: Node) : Node() {
    override fun toString(): String {
        return "UnaryOp${op.value} (\n$expr)"
    }
}

class BinOp(val left: Node, val op: Token, val right: Node) : Node() {
    override fun toString(): String {
        return "BinOp${op.value} (\n$left, $right)"
    }
}

class Cover(val expressions: ArrayList<Node>) : Node() {
    override fun toString(): String {
        return "Cover (\n$expressions)"
    }
}
