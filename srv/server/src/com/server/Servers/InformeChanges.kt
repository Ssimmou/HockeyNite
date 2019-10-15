import com.example.Data.Games
import com.google.gson.GsonBuilder
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.PrintWriter
import java.net.Socket

class InformeChanges(socket: Socket, gameId: Integer): Runnable{
    var socket = socket
    var gameId = gameId
    override fun run() {
        var `is` = DataInputStream(socket.getInputStream())
        var os = DataOutputStream(socket.getOutputStream())

        val gson = GsonBuilder().setPrettyPrinting().create()
        val str = gson.toJson(Games.getGame(gameId as Int)) as String

        val pw = PrintWriter(os)

        pw.println(str)
        pw.flush()
    }

}