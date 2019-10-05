package com.example.Data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Period : Table("period") {
    val id = integer("id").primaryKey().autoIncrement();
    val gameId = (integer("gameId")references  Game.id).nullable()
    val ended = (integer("ended"))
}

data class Periods(val id: Int, val gameId: Int, val ended : Int) {
    companion object {

        @JvmStatic
        @Synchronized
        fun periodEnded(id: Int, matchEnded: Boolean) {
            transaction {
                var periodId = lastPeriod(id) as Int
                Period.update({ Period.id eq periodId }) {
                    it[Period.ended] = 1
                }
            }
            if (!matchEnded) {
                addPeriod(id)
            }
        }
        @JvmStatic
        @Synchronized
        fun addPeriod(id: Int) {
            transaction {
                Period.insert {
                    it[Period.gameId] = id
                }
            }
        }

        @JvmStatic
        @Synchronized
        fun lastPeriod(gameId: Int): Int {
            var ret = 0
            transaction {
                var res = Period.select {
                    Period.gameId.eq(gameId)
                }
                for(row in res){
                    if(ret < row[Period.id])
                        ret = row[Period.id]
                }
                println(ret)
            }
            return ret
        }
    }
}


