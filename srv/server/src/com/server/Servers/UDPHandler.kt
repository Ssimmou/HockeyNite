package com.server.Servers

import com.example.Data.Games
import com.example.Data.getGame
import com.google.gson.GsonBuilder
import com.server.com.server.Data.ListGames
import com.server.com.server.Reply
import com.server.com.server.Request
import com.server.com.server.Servers.Serialize
import com.server.com.server.Servers.unSerialize
import kotlinx.coroutines.delay
import placeBet
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.io.IOException
import java.net.Socket
import java.net.SocketException




class UDPHandler(datagram: DatagramPacket, sock: DatagramSocket) : Runnable {

    var datagramPacket : DatagramPacket = datagram
    var serverSocket : DatagramSocket = sock
    override fun run() {
        var message = unSerialize(datagramPacket)
        var rep : Reply? = null
        println(message.option)
        if(message.option == Request.Option.list){
            rep = Reply(datagramPacket.address, datagramPacket.port, ListGames().getAllGames())
        }
        if(message.option == Request.Option.detail){
            var id = (message.argument?.get(0) as Double).toInt()
            rep = getGame(id)?.let { Reply(datagramPacket.address, datagramPacket.port, it) }
        }
        if(message.option == Request.Option.betInfo){
            println(message.argument)
            var id =(message.argument?.get(0) as Double).toInt()
            var choice = (message.argument?.get(1) as Double).toInt()
            var bet = (message.argument?.get(2) as Double).toFloat()
            rep = Reply(datagramPacket.address, datagramPacket.port, placeBet(id, choice, bet))
        }
        if (rep != null) {
            send(rep)
        }
    }

     fun send(reply : Reply){
        //Message.getData()
        val toSend = Serialize(reply)
        val datagram = toSend?.size?.let {
            DatagramPacket(
                toSend,
                it,
                reply.destination,
                reply.destinationPort
            )
        }
        Thread.sleep(1000)
        serverSocket.send(datagram)
    }
}
