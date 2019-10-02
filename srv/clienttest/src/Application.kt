package com.example

import io.ktor.application.*
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.util.cio.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress

fun main(args: Array<String>) {
    runBlocking {
        val socket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp().connect(InetSocketAddress("127.0.0.1", 2323))
        val input = socket.openReadChannel()
        val output = socket.openWriteChannel(autoFlush = true)

        output.write("getMatchs\r\n")
        val response = input.readUTF8Line()
        println("Server said: '$response'")
    }
}