package com.server.Servers

import com.server.com.server.Servers.TCPHandler
import kotlinx.coroutines.delay
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.io.IOException
import java.net.SocketException





class TCPServer(portTCP: Int, threadPoolSizeTCP: Int) {
   

    private var serverSocket: ServerSocket? = null
    private var poolSize: Int = 0
    private var serverPort : Int = 0

    init {
        serverPort = portTCP
        poolSize = threadPoolSizeTCP
    }
    suspend fun start() {
        println("TCPSERVER STARTED")
        val threadPool = Executors.newFixedThreadPool(poolSize)
        serverSocket = ServerSocket(serverPort)
        for (i in 0..9) {
            delay(200)
            var socket = serverSocket!!.accept()
            threadPool.execute(TCPHandler(socket))
        }
        threadPool.shutdown()
    }
}
