import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction



data class Bets(val id: Int, val idGame : Int, val choice :Int, val bet : Float){

}

