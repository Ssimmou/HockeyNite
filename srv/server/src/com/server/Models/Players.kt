package com.server.com.server.Models

import com.example.Data.Team
import org.jetbrains.exposed.sql.Table

object Player : Table("player") {
    val id = Player.integer("id").primaryKey().autoIncrement();
    val name = Player.varchar("name", length = 50)
    val idTeam = (Player.integer("idTeam")references Team.id).nullable()
}

data class Players(val id: Int, val name: String, val teamId : Int)
