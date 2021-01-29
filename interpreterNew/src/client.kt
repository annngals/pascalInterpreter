import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.Socket

class Client (private val ip: String, private val port: Int){
    private lateinit var socket: Socket
    private lateinit var inStream: BufferedReader
    private lateinit var outStream: BufferedWriter

    fun connect() {
        socket = Socket(ip, port)
        inStream = socket.getInputStream().bufferedReader()
        outStream = socket.getOutputStream().bufferedWriter()
    }

    fun send(text: String) {
        outStream.write("$text\n")
        outStream.flush()
    }

    fun recv(): String {
        return inStream.readLine()
    }
}

fun main(args: Array<String>){
    val client = Client("127.0.0.1", 5555)
    client.connect()
    client.send("2 + 2")
    println(client.recv())
}