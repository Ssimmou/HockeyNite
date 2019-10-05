package com.example.Data
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


data class Goals(val id: Int,  val teamId :Int, val playerId : Int, val periodId : Int){
}

