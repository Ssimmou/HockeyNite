package com.example.Data

import com.example.Data.Goal.nullable
import com.example.Data.Goal.references
import com.server.com.server.Models.Player
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object Penalty : Table("penalty") {
    val id = integer("id").primaryKey().autoIncrement();
    val teamId = (integer("teamId") references  Team.id).nullable()
    val playerId = (integer("playerId") references  Player.id).nullable()
    val periodId = (integer("periodId")references  Period.id).nullable()


}


data class Penalties(val id: Int, val teamId: Int, val playerId : Int, val periodId: Int){
    companion object{
        @JvmStatic
        @Synchronized
        fun addPenalty(gameId: Int, teamId: Int, playerId : Int) {
            transaction{
                Penalty.insert {
                    it[Penalty.teamId] = teamId
                    it[Penalty.playerId] = playerId
                    it[Penalty.periodId] = Periods.lastPeriod(gameId)
                }
            }
        }
        @Synchronized
        @JvmStatic
        fun getPenalitiesInMatch(idGame: Int, idTeam: Int?) : Int{
            var ret : Int = 0
            var res : Query? = null
            transaction{
                res = Period.select {
                    Period.gameId.eq(idGame)
                }
            }
            for(row in res!!){
                transaction {
                    var resu = Penalty.select {
                        Penalty.periodId.eq(row[Period.id])and Penalty.teamId.eq(idTeam)
                    }
                    for(ro in resu)
                        ret++
                }
            }
            return ret
        }
    }
}

