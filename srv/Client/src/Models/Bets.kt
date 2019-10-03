import Models.Game
import org.jetbrains.exposed.sql.Table

object Goal : Table("bet") {
    val id = integer("id").primaryKey().autoIncrement()
    val idGame = (integer("idGame") references Game.id).nullable()
    val choice = integer("choice")
    val bet = float("bet")
}

data class Bets(val id: Int, val idGame : Int, val choice :Int, val bet : Float)

