import java.io.File
import java.net.ServerSocket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class Server (private val port: Int) {
    private val serverSocket = ServerSocket(port)

    fun start() {
        val interpreter = Interpreter()
        Logger.attach(ConsoleHandler())
        Logger.attach(FileHandler(File("C://Projects//Idea//inter//log.txt")))

        while (true){
            Logger.info("Wait for new connection")
            val clientSocket = serverSocket.accept()
            clientSocket.soTimeout = 30000
            Logger.info("Process new client ${clientSocket.localAddress}")
            val inStream = clientSocket.getInputStream().bufferedReader()
            val outStream = clientSocket.getOutputStream().bufferedWriter()
            while (!clientSocket.isOutputShutdown){
                try {
                    Logger.info("Wait for new message")
                    val data = inStream.readLine()
                    if (data != null){
                        Logger.info(data)
                        val result = interpreter.interpret(Parser(Lexer(data)).expr())
                        outStream.write("Result = $result\n")
                        outStream.flush()
                    }
                    else {
                        outStream.write("error\n")
                        outStream.flush()
                        Logger.info("Client closed connection")
                        break
                    }
                }
                catch (e: SocketTimeoutException) {
                    Logger.warning("Disconnect client timeout")
                    outStream.flush()
                    clientSocket.close()
                    break
                }
            }
        }
    }
}

fun main (args: Array<String>) {
    thread {
        Server(5555).start()
    }
    thread {
        val client = Client("127.0.0.1", 5555)
        client.connect()
        client.send("2 + 2")
        Thread.sleep(30000)
        println(client.recv())
        client.send("2 + 2 / 3 + 2 * (3 - 5)")
        println(client.recv())
    }
    thread {
        val client = Client("127.0.0.1", 5555)
        client.connect()
        client.send("2 + 4")
        Thread.sleep(30000)
        println(client.recv())
        client.send("5 * 5")
        println(client.recv())
    }
}