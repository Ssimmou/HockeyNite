package Models
import org.jetbrains.exposed.sql.Table

/**Period data Class
 * @author Soufiane SIMMOU
 * @version V0.0.0
 */

/**
 * Setting up the table skeleton for the dataBase
 */
object Period : Table("period") {
    val id = integer("id").primaryKey().autoIncrement();
    val gameId = (integer("gameId")references  Game.id).nullable()

}

data class Periods(val id: Int, val gameId: Int)