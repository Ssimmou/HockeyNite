package com.example.Data
import com.server.com.server.Models.Player
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Team : Table("team") {
    val id = Team.integer("id").primaryKey().autoIncrement();
    val name = Team.varchar("name", length = 50)
}

data class Teams(val id: Int, val name: String){
    companion object{
        @JvmStatic
        @Synchronized
        fun getTeam(id: Int) : String{
            var name : String = ""
            transaction {
                var res = Team.select {
                    Team.id.eq(id)
                }
                for (row in res){
                    name += row[Team.name]
                }
            }
            return name
        }
    }
}

