package Models
import org.jetbrains.exposed.sql.Table
/**Penalties data Class
 * @author Soufiane SIMMOU
 * @version V0.0.0
 */

/**
 * Setting up the table skeleton for the dataBase
 */

object Penalty : Table("penalty") {
    val id = integer("id").primaryKey().autoIncrement();
    val chrono = (long("chrono"))
    val teamId = (integer("teamId") references Team.id).nullable()
    val periodId = (integer("periodId")references Period.id).nullable()


}


data class Penalties(val id: Int, val chrono: Long, val teamId: Int, val periodId: Int)

