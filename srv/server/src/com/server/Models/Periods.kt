package com.example.Data

import org.jetbrains.exposed.sql.Table

object Period : Table("period") {
    val id = integer("id").primaryKey().autoIncrement();
    val gameId = (integer("gameId")references  Game.id).nullable()

}

data class Periods(val id: Int, val gameId: Int)