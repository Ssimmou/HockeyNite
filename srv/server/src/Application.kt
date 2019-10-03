package com.server

import com.example.Data.Game
import com.example.Data.Games
import com.server.Servers.TCPServer
import com.server.Servers.UDPServer
import javafx.application.Application.launch
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


fun initDB() {
    val url = "jdbc:mysql://root:@localhost:3306/tests?useUnicode=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    Database.connect(url, driver)
}

fun main() = runBlocking<Unit> {

    initDB()



    launch{
        val port = 6780
        val threadPoolSize = 4
        var udpServer = UDPServer(port, threadPoolSize)
        udpServer.start()
    }

    /*launch{
        val portTCP = 1248
        val threadPoolSizeTCP = 10
        val tcpServer = TCPServer(portTCP, threadPoolSizeTCP)
        tcpServer.start()
    }*/

}



