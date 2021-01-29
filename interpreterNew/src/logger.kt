import java.io.File
import java.util.*
import kotlin.collections.ArrayList

enum class MessageType {
    CRITICAL,
    INFO,
    WARNING
}

interface Handler {

    fun log(message: String, type: MessageType)

}

class ConsoleHandler(): Handler {
    override fun log(message: String, type: MessageType) {
        if (type == MessageType.CRITICAL){
            System.err.println(message)
        }
        else{
            println(message)
        }
    }
}

class FileHandler(val file: File): Handler {
    override fun log(message: String, type: MessageType) {
        file.appendText(message)
    }
}

object Logger{
    val handlers = ArrayList<Handler>()

    fun attach(handler: Handler) {
        handlers.add(handler)
    }

    fun detach(handler: Handler) {
        handlers.remove(handler)
    }

    fun log(message: String, type: MessageType) {
        val date = Date()
        val stack = Thread.currentThread().stackTrace
        val element = stack[2]
        val callerClass = element.className
        val callerMethod = element.methodName
        val line = element.lineNumber
        for (handler in handlers) {
            val msg = "$date $callerClass $callerMethod $line $message"
            handler.log(msg, type)
        }
    }

    fun warning(message: String) {
        log(message, MessageType.WARNING)
    }

    fun critical(message: String) {
        log(message, MessageType.CRITICAL)
    }

    fun info(message: String) {
        log(message, MessageType.INFO)
    }
}

fun main(args: Array<String>) {
    Logger.attach(ConsoleHandler())
    Logger.attach(ConsoleHandler())
    Logger.attach(FileHandler(File("C://Projects//Idea//inter//log.txt")))
    Logger.log("Hello logger\n", MessageType.CRITICAL)
}
