package com.server.com.server.Servers

import com.example.Data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class GamesMAJ {

    val sc = Scanner(System.`in`)

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
    }

    private fun endPeriod() {
        println("Enter the game id : ")
        val gameId = sc.nextInt()
        Periods.periodEnded(gameId, false)

    }

    private fun addGoal() {
        println("Enter the gameId teamId playerId : ")
        val gameId = sc.nextInt()
        val teamId = sc.nextInt()
        val playerId = sc.nextInt()

        Goals.addGoal(gameId, teamId, playerId)
    }



    private fun endGame() {
        println("Enter the game id : ")
        val gameId = sc.nextInt()
        Games.gameEnded(gameId)
    }
}
