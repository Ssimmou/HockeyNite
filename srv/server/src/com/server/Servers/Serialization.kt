package com.server.com.server.Servers

import Bets
import com.google.gson.GsonBuilder
import com.server.com.server.Request
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.net.DatagramPacket

fun unSerialize(packet: DatagramPacket) : Request {
    val gson =  GsonBuilder().setPrettyPrinting().create()
    var response = String(packet.getData(), 0, packet.getLength())
    response = response.subSequence(response.indexOf('{'), response.length) as String
    var request: Request =  gson.fromJson(response, Request::class.java)
    return request;
}

fun Serialize(request : Any): ByteArray? {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeBytes(gson.toJson(request))
    oos.close()
    return baos.toByteArray()
}



