package com.example.Data

import org.jetbrains.exposed.sql.Table

import org.joda.time.DateTime

object Game : Table("game") {
    val id = integer("id").primaryKey().autoIncrement();
    val team1Id = (integer("team1Id")).nullable()
    val team2Id = (integer("team2Id")).nullable()
    val date = (date("date").nullable())
}
data class Games(val id:Int, val team1Id: Int?, val team2Id: Int?, val date: DateTime?)

data class DetailGame(val team1Name: String, val team2Name: String, val date: DateTime?)
