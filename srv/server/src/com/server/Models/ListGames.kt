package com.server.com.server.Data

import com.example.Data.Game
import com.example.Data.Games
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ListGames{
    companion object{
        @JvmStatic
        @Synchronized
        fun getAllGames(): ArrayList<Games> {
            val c = ArrayList<Games>()
            transaction {
                var res = Game.selectAll().limit(5)
                for (f in res) {
                    c.add(Games(id = f[Game.id], team1Id = f[Game.team1Id], team2Id = f[Game.team2Id], date = f[Game.date], ended = f[Game.ended]))
                }
            }
            return c
        }
    }


}