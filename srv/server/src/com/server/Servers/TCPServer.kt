package com.server.Servers

import Scope
import com.server.com.server.Servers.TCPHandler
import io.ktor.util.Hash
import kotlinx.coroutines.delay
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import java.io.IOException
import java.net.SocketException





class TCPServer(portTCP: Int, threadPoolSizeTCP: Int, app : Scope) {
   

    private var serverSocket: ServerSocket? = null
    private var poolSize: Int = 0
    private var serverPort : Int = 0
    var mainApp : Scope? = null
    init {
        serverPort = portTCP
        poolSize = threadPoolSizeTCP
        mainApp = app
    }
    suspend fun start() {
        println("TCPSERVER STARTED")
        val threadPool = Executors.newFixedThreadPool(poolSize)
        serverSocket = ServerSocket(serverPort)
        for (i in 0..9) {
            delay(200)
            var socket = serverSocket!!.accept()
            threadPool.execute(TCPHandler(socket, this))
        }
        threadPool.shutdown()
    }
}
