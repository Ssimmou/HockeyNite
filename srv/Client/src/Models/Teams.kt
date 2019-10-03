package Models
import org.jetbrains.exposed.sql.Table

/**Teams data Class
 * @author Soufiane SIMMOU
 * @version V0.0.0
 */

/**
 * Setting up the table skeleton for the dataBase
 */
object Team : Table("team") {
    val id = Team.integer("id").primaryKey().autoIncrement();
    val name = Team.varchar("name", length = 50)
}

data class Teams(val id: Int, val name: String)

