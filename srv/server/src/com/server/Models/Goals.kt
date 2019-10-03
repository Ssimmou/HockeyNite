package com.example.Data
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction


object Goal : Table("goal") {
    val id = integer("id").primaryKey().autoIncrement();
    val chrono = long("chrono")
    val teamId = (integer("teamId")references  Team.id).nullable()
    val periodId = (integer("periodId")references  Period.id).nullable()
}


data class Goals(val id: Int, val chrono : Long, val teamId :Int, val periodId : Int)

fun getGoalsInMatch(idGame: Int, idTeam: Int?) : Int{
    var ret : Int = 0
    var res : Query? = null
    transaction{
        res = Period.select {
            Period.gameId.eq(idGame)
        }
    }
    for(row in res!!){
        transaction {
            var resu = Goal.select {
                Goal.periodId.eq(row[Period.id])and Goal.teamId.eq(idTeam)
            }
            for(ro in resu)
                ret++
        }
    }
    return ret
}