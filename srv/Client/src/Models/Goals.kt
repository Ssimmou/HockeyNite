package Models
import org.jetbrains.exposed.sql.Table

/**Goals data Class
 * @author Soufiane SIMMOU
 * @version V0.0.0
 */

/**
 * Setting up the table skeleton for the dataBase
 */

object Goal : Table("goal") {
    val id = integer("id").primaryKey().autoIncrement();
    val chrono = long("chrono")
    val teamId = (integer("teamId")references Team.id).nullable()
    val periodId = (integer("periodId")references Period.id).nullable()
}


data class Goals(val id: Int, val chrono : Long, val teamId :Int, val periodId : Int)

