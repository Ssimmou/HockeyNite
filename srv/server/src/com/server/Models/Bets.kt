import com.example.Data.Game
import com.example.Data.Period
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Bet : Table("bet") {
    val id = integer("id").primaryKey().autoIncrement()
    val idGame = (integer("idGame") references Game.id).nullable()
    val choice = integer("choice")
    val bet = float("bet")
}
/**
 * Bets.kt
 *
 * This class Creates the Bet Object and handles all the operations related.
 * @property id The bet ID
 * @property idGame The game ID
 * @property choice The team that you want to bet on 0 for home 1 for away team
 * @property bet    The amount of the bet made
 * @constructor Creates an empty bet
 */

data class Bets(val id: Int, val idGame : Int, val choice :Int, val bet : Float){
    companion object{
        /**
         * PlaceBet, Places a bet and stores it in the DataBase
         * @return done if everything goes well, error otherwise
         */
        @Synchronized fun placeBet(id: Int, choix: Int, amount: Float) : String{
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
                    println(row[Period.id])
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
        /**
         * getWinningSum
         *
         * @return the sum of wining bets only
         */
        fun getWinningSum(gameId: Int, gameRes: Int): Double {
            var sum : Double = 0.0
            transaction {
                var res = Bet.select {
                    Bet.idGame.eq(gameId)and Bet.choice.eq(gameRes)
                }
                for(row in res){
                    sum += row[Bet.bet]
                }
            }
            println("winning sum = " + sum)
            return sum
        }
        /**
         * getSum
         * @return the sum of all the placed bets.
         */
        fun getSum(gameId: Int): Double {
            var sum : Double = 0.0
            transaction {
                var res = Bet.select {
                    Bet.idGame.eq(gameId)
                }
                for(row in res){
                    sum += row[Bet.bet]
                }
            }
            println("sum = " + sum)
            return sum
        }
    }
}

