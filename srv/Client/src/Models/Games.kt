package com.example.Data

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.util.*


data class Games(val id:Int, val team1Id: Int?, val team2Id: Int?, val date: String, val ended : Int) {

}

data class DetailGame(val team1Name: String, val team2Name: String, val date: Date?, val team1Goals : Int, val team2Goals: Int, val team1Penalties : Int, val team2Penalties: Int, val isEnded : Int){

}




