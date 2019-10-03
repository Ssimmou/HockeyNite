import com.example.Data.Game
import com.example.Data.Period
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Bet : Table("bet") {
    val id = integer("id").primaryKey().autoIncrement()
    val idGame = (integer("idGame") references Game.id).nullable()
    val choice = integer("choice")
    val bet = float("bet")
}

data class Bets(val id: Int, val idGame : Int, val choice :Int, val bet : Float)

fun placeBet(id : Int,choix : Int,amount : Float) : String{
    println(id)
    println(choix)
    println(amount)
    var res : Query? = null
    var count = 0
    transaction {
        res = Period.select {
            Period.gameId.eq(id)
        }
        for(row in res!!){
            count++
        }
    }
    println(count)
    if(count > 2){
        return "error"
    }
    transaction {
        Bet.insert {
            it[idGame] = id
            it[bet] = amount
            it[choice] = choix
        }
    }
    return "done"
}