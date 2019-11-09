package com.server.com.server.Data

import com.example.Data.Game
import com.example.Data.Games
import com.example.Data.Teams
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
/**
 * ListGames.kt
 *
 * This class is a just needed for it static function getAllGames
 * returns a list of games in the dataBase
 */

class ListGames{
    companion object{
        @JvmStatic
        @Synchronized
        fun getAllGames(): ArrayList<Any> {
            val c = ArrayList<Any>()
            transaction {
                var res = Game.selectAll().limit(5)
                for (f in res) {
                    //if(f[Game.date]?.toDate()!!.day == Date.from(Instant.now()).day)
                    //c.add(Games(id = f[Game.id], team1Id = f[Game.team1Id], team2Id = f[Game.team2Id], date = f[Game.date].toString(), ended = f[Game.ended]))
                    c.add(Games(id = f[Game.id], team1Id = f[Game.team1Id], team2Id = f[Game.team2Id], date = f[Game.date].toString(), ended = f[Game.ended]))
                    //c.add(Teams.getTeam(f[Game.team1Id] as Int))
                    //c.add(Teams.getTeam(f[Game.team2Id] as Int))
                }
            }
            return c
        }
    }


}