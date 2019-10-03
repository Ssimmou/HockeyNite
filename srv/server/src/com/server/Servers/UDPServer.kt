package com.server.Servers

import kotlinx.coroutines.delay
import java.io.IOException
import java.net.DatagramPacket
import java.net.SocketException
import java.net.DatagramSocket
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService



class UDPServer(port: Int, threadPoolSize: Int) {


    private var serverSocket: DatagramSocket? = null
    private var poolSize: Int = 0
    private var serverPort : Int = 0
    init{
        poolSize = threadPoolSize
        serverPort = port
    }

    suspend fun start() {
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
