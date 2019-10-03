package com.example.Data
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object Team : Table("team") {
    val id = Team.integer("id").primaryKey().autoIncrement();
    val name = Team.varchar("name", length = 50)
}

data class Teams(val id: Int, val name: String)

fun getTeam(team2Id: Int): String {
    var str : String = ""
    transaction {
        var res = Team.select{
            Team.id.eq(team2Id)}
        for (f in res) {
            str = f[Team.name]
        }
    }
    return str
}