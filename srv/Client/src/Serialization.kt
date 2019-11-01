
import com.google.gson.GsonBuilder
import java.io.*
import java.net.DatagramPacket


fun unSerialize(packet: DatagramPacket) : Request {
    val gson =  GsonBuilder().setPrettyPrinting().create()
    var response = String(packet.getData(), 0, packet.getLength())
    response = response.subSequence(response.indexOf('{'), response.length) as String
    var request: Request =  gson.fromJson(response, Request::class.java)
    return request;
}

fun unSerializeReply(packet: DatagramPacket) : Reply {
    val gson =  GsonBuilder().setPrettyPrinting().create()
    var response = String(packet.getData(), 0, packet.getLength())
    response = response.subSequence(response.indexOf('{'), response.length) as String
    println(response)
    File("data.json").writeText(response)

    var reply: Reply =  gson.fromJson(response, Reply::class.java)
    return reply;
}

fun serialize(request : Request): ByteArray? {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeBytes(gson.toJson(request))
    oos.close()
    return baos.toByteArray()
}

