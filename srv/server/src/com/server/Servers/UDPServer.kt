package com.server.Servers

import Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.SocketException
import java.net.DatagramSocket
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import kotlin.coroutines.coroutineContext


class UDPServer(port: Int, threadPoolSize: Int, app : Scope) {

    private var serverSocket: DatagramSocket? = null
    private var poolSize: Int = 0
    private var serverPort : Int = 0
    init{
        poolSize = threadPoolSize
        serverPort = port
    }

    suspend fun start()  = withContext(Dispatchers.Default) {
        println("UDPSERVER STARTED")
        var threadPool = Executors.newFixedThreadPool(poolSize)
        serverSocket = DatagramSocket(serverPort)
        val buffer = ByteArray(4000)
        for (i in 0..9) {
            val datagram = DatagramPacket(buffer, buffer.size)
            serverSocket!!.receive(datagram)
            threadPool.execute(UDPHandler(datagram, serverSocket!!))
        }
        threadPool.shutdown()
    }
}
