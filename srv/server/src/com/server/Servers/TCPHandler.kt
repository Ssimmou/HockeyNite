package com.server.com.server.Servers

import java.net.Socket
import Bet
import Bets
import com.google.gson.GsonBuilder
import com.server.Servers.TCPServer
import com.server.com.server.Request
import java.io.*
import java.util.*


class TCPHandler(val socket: Socket, val server : TCPServer) : Runnable{
    private var connectionSocket: Socket? = null
    init {
        connectionSocket = socket
    }

    override fun run() {
        val `is` = connectionSocket?.getInputStream()
        val bis = BufferedInputStream(`is`)


        val buff = Scanner(bis)

        var currentBetBytes = ""
        while(buff.hasNext()) {

            var str = buff.nextLine() //We wait for the object
            currentBetBytes += str
            str = str.trim()
            if(str == "}")
                break
        }
        val gson =  GsonBuilder().setPrettyPrinting().create()
        var currentBet: Bets =  gson.fromJson(currentBetBytes, Bets::class.java)
        val res = Bets.placeBet(currentBet.idGame, currentBet.choice, currentBet.bet)
        println(res)
        val os = connectionSocket?.getOutputStream()
        val pw = PrintWriter(os)
        if (res == "error") {
            if (os != null) {
                pw.println(1)
            }
        } else {
            if (os != null) {
                pw.println(0)
                if(server.mainApp?.list?.containsKey(currentBet.idGame as Integer)!!)
                    server.mainApp?.list?.get(currentBet.idGame as Integer)?.add(socket)
                else {
                    var list = ArrayList<Socket>()
                    list.add(socket)
                    server.mainApp!!.list.put(currentBet.idGame as Integer, list)
                }
            }
        }

        if (os != null) {
            pw.flush()
        }
    }

}