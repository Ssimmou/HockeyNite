package com.example.Data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.util.*

object Game : Table("game") {
    val id = integer("id").primaryKey().autoIncrement();
    val team1Id = (integer("team1Id")).nullable()
    val team2Id = (integer("team2Id")).nullable()
    val date = (date("date").nullable())
    val ended = (integer("ended"))
}
data class Games(val id:Int, val team1Id: Int?, val team2Id: Int?, val date: String, val ended : Int) {
    companion object {

        @JvmStatic
        @Synchronized
        fun gameEnded(id: Int) {
            transaction {
                Game.update({ Game.id eq id }) {
                    it[ended] = 1
                }
            }
            Periods.periodEnded(id, true)
        }
        @JvmStatic
        @Synchronized fun getGame(id : Int): DetailGame? {
            var team1Id : Int = 0
            var team2Id : Int = 0
            var detail : DetailGame? = null
            transaction {
                var res = Game.select{
                    Game.id.eq(id)
                }
                for(row in res){
                    detail = DetailGame(Teams.getTeam(row[Game.team1Id] as Int), Teams.getTeam(row[Game.team2Id] as Int), row[Game.date].toString(),
                        Goals.getGoalsInMatch(id, row[Game.team1Id]), Goals.getGoalsInMatch(id, row[Game.team2Id]),
                        Penalties.getPenalitiesInMatch(id, row[Game.team1Id]), Penalties.getPenalitiesInMatch(id, row[Game.team2Id]), row[Game.ended]
                    )
                }
            }
            return detail
        }
        @JvmStatic
        @Synchronized fun getResult(gameId: Int): Int {
            var team1Goals : Int = 0
            var team2Goals : Int = 0
            transaction {
                var res = Game.select{
                    Game.id.eq(gameId)
                }
                for(row in res){
                    team1Goals = Goals.getGoalsInMatch(gameId, row[Game.team1Id])
                    team2Goals = Goals.getGoalsInMatch(gameId, row[Game.team2Id])
                }
            }
            if(team1Goals == team2Goals)
                return 0
            if(team1Goals > team2Goals)
                return 1
            return 2
        }
    }

}

data class DetailGame(val team1Name: String, val team2Name: String, val date: String, val team1Goals : Int, val team2Goals: Int, val team1Penalties : Int, val team2Penalties: Int, val isEnded : Int){
    companion object{

    }
}




