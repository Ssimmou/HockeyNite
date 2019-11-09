package com.server.com.server.Servers

import InformeChanges
import Scope
import com.example.Data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.Executors

class GamesMAJ(var app : Scope) {

    val sc = Scanner(System.`in`)
    val threadPool = Executors.newFixedThreadPool(10)

    suspend fun start() = withContext(Dispatchers.Default){
        println("GAMESMAJ STARTED")
        while(true){
            println("1 - Mark game as ended")
            println("2 - Mark Period as Ended")
            println("3 - Add Goal")
            println("4 - Add Penalty")


            var sc = Scanner(System.`in`)
            var choice = sc.nextInt()

            if(choice == 1){
                endGame()
                continue
            }
            if(choice == 2){
                endPeriod()
                continue
            }
            if(choice == 3){
                addGoal()
                continue
            }
            if(choice == 4){
                addPenalty()
                continue
            }
            println("Bad choice")
        }
    }


    private fun addPenalty() {
        println("Enter the gameId teamId playerId : ")
        val gameId = sc.nextInt()
        val teamId = sc.nextInt()
        val playerId = sc.nextInt()

        Penalties.addPenalty(gameId, teamId, playerId)
        if(app.list.containsKey(gameId as Integer)){
            for(socket in app.list.get(gameId as Integer)!!){
                threadPool.execute(InformeChanges(socket, gameId, 0))
            }
        }
    }

    private fun endPeriod(): String {
        println("Enter the game id : ")

        val gameId = sc.nextInt()
        var res : Query? = null
        var count = 0
        transaction {
            res = Period.select {
                Period.gameId.eq(gameId)
            }
            for(row in res!!){
                count++
                //println(row[Period.id])
            }
        }
        println(count)
        if(count > 2){
           print("you can't end period,it is grater than 2 the game should be ended")
            return "error"
        }
        Periods.periodEnded(gameId, false)
        if(app.list.containsKey(gameId as Integer)){
            for(socket in app.list.get(gameId as Integer)!!){
                threadPool.execute(InformeChanges(socket, gameId, 0))
            }
        }
        return "OK"
    }

    private fun addGoal() {
        println("Enter the gameId teamId playerId : ")
        val gameId = sc.nextInt()
        val teamId = sc.nextInt()
        val playerId = sc.nextInt()

        Goals.addGoal(gameId, teamId, playerId)
        if(app.list.containsKey(gameId as Integer)){
            for(socket in app.list.get(gameId as Integer)!!){
                threadPool.execute(InformeChanges(socket, gameId, 0))
            }
        }
    }



    private fun endGame() {
        println("Enter the game id : ")
        val gameId = sc.nextInt()
        Games.gameEnded(gameId)
        if(app.list.containsKey(gameId as Integer)){
            for(socket in app.list.get(gameId as Integer)!!){
                threadPool.execute(InformeChanges(socket, gameId, 1))
            }
        }
    }
}
