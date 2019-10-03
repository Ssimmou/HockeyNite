package com.server.com.server

import java.io.Serializable
import java.net.InetAddress


class Request : Serializable{

    //protected var type: Int = 0

    var destination: InetAddress? = null
    var destinationPort: Int = 0
    var argument: ArrayList<Any>? = null
    var option: Option? = null
    enum class Option {
        list, detail, betInfo
    }

    //private val serialVersionUID = 361933411720337979L

    //private val MAX_NUM = 50000
    //private var numRequest = 0




    /*@Synchronized
    fun craftGetMatchList(adress: InetAddress, port: Int): Request {
        val request = Request()
        request.methode = methodes.list
        request.setNumero(numRequest)
        request.setDestinationPort(port)
        request.setDestination(adress)
        incrementNumRequest()
        return request
    }

    @Synchronized
    fun craftGetMatchDetail(adress: InetAddress, port: Int, idMatch: Int): Request {
        val request = Request()
        request.methode = methodes.detail
        val arg = arrayOf<Any>(idMatch)
        request.argument = arg
        request.setDestinationPort(port)
        request.setDestination(adress)
        request.setNumero(numRequest)
        incrementNumRequest()
        return request
    }

    @Synchronized
    fun craftGetBetInfo(adress: InetAddress, port: Int, idMatch: Int, idBet: String): Request {
        val request = Request()
        request.methode = methodes.betInfo
        val arg = arrayOf<Any>(idMatch, idBet)
        request.argument = arg
        request.setDestinationPort(port)
        request.setDestination(adress)
        request.setNumero(numRequest)
        incrementNumRequest()
        return request
    }

    internal fun incrementNumRequest() {
        if (numRequest == MAX_NUM) {
            numRequest = 0
        }
        numRequest++
    }*/
}
