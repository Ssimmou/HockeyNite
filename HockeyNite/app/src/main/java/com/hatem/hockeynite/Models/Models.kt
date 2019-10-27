package com.hatem.hockeynite.Models

import java.util.*


data class Goals(val id: Int,  val teamId :Int, val playerId : Int, val periodId : Int){
}
data class Bets(val id: Int, val idGame : Int, val choice :Int, val bet : Float){

}
class ListGames{
}
data class Penalties(val id: Int, val teamId: Int, val playerId : Int, val periodId: Int){
}
data class Periods(val id: Int, val gameId: Int, val ended : Int) {
}
data class Players(val id: Int, val name: String, val teamId : Int)

data class Result(val sum : Double, val winningSum : Double, val res : Int)
data class Teams(val id: Int, val name: String){
}
data class Games(val id:Int, val team1Id: Int?, val team2Id: Int?, val date: String, val ended : Int) {

}

data class DetailGame(val team1Name: String, val team2Name: String, val date: Date?, val team1Goals : Int, val team2Goals: Int, val team1Penalties : Int, val team2Penalties: Int, val isEnded : Int){

}
