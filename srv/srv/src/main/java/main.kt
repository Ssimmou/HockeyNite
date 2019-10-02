import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction



object Penalty : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val type = varchar("type", 50);
    val chrono = long("chrono");
    val teamId = integer("teamId")// Column<Int?>
    val periodId = (integer("periodId")) // Column<Int?>
}



object Goal : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val chrono = long("chrono");
    val teamId = (integer("teamId")) // Column<Int?>
    val periodId = (integer("periodId")) // Column<Int?>
}

object Period : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val gameId = (integer("gameId")) // Column<Int?>
}

object Game : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val chrono = long("chrono");
    val team1Id = (integer("team1Id")) // Column<Int?>
    val team2Id = (integer("team2Id")) // Column<Int?>

}

object Team : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val name = varchar("name", 50);
}

fun initDB() {
    val url = "jdbc:mysql://root:web@localhost:3306/tests?useUnicode=true&serverTimezone=UTC"
    val driver = "com.mysql.cj.jdbc.Driver"
    Database.connect(url, driver)
    transaction {
        SchemaUtils.create(Game, Goal, Period, Penalty, Team);
    }
}

fun addTeam(str : String): String {
    transaction {
        Team.insert {
            it[name] = str
        }
    }
    return "true"
}


fun main(args: Array<String>) {

    initDB()
    val server = embeddedServer(Netty, port = 8080) {


        routing {
            get("/addTeam/{name}") {
                val str = call.parameters["name"].toString()
                call.respondText(addTeam(str), ContentType.Text.Plain)
            }

            get("/allTeams") {
                transaction {
                    for (team in Team.selectAll()) {
                        println("${team[Team.id]}: ${team[Team.name]}")
                    }                }
            }
        }
    }
    server.start(wait = true)

    /*transaction {
        SchemaUtils.create (Cities, Users)

        val saintPetersburgId = Cities.insert {
            it[name] = "St. Petersburg"
        } get Cities.id

        val munichId = Cities.insert {
            it[name] = "Munich"
        } get Cities.id

        Cities.insert {
            it[name] = "Prague"
        }

        Users.insert {
            it[id] = "andrey"
            it[name] = "Andrey"
            it[cityId] = saintPetersburgId
        }

        Users.insert {
            it[id] = "sergey"
            it[name] = "Sergey"
            it[cityId] = munichId
        }

        Users.insert {
            it[id] = "eugene"
            it[name] = "Eugene"
            it[cityId] = munichId
        }

        Users.insert {
            it[id] = "alex"
            it[name] = "Alex"
            it[cityId] = null
        }

        Users.insert {
            it[id] = "smth"
            it[name] = "Something"
            it[cityId] = null
        }

        Users.update({Users.id eq "alex"}) {
            it[name] = "Alexey"
        }

        Users.deleteWhere{Users.name like "%thing"}

        println("All cities:")

        for (city in Cities.selectAll()) {
            println("${city[Cities.id]}: ${city[Cities.name]}")
        }

        println("Manual join:")
        (Users innerJoin Cities).slice(Users.name, Cities.name).
            select {(Users.id.eq("andrey") or Users.name.eq("Sergey")) and
                    Users.id.eq("sergey") and Users.cityId.eq(Cities.id)}.forEach {
            println("${it[Users.name]} lives in ${it[Cities.name]}")
        }

        println("Join with foreign key:")


        (Users innerJoin Cities).slice(Users.name, Users.cityId, Cities.name).
            select {Cities.name.eq("St. Petersburg") or Users.cityId.isNull()}.forEach {
            if (it[Users.cityId] != null) {
                println("${it[Users.name]} lives in ${it[Cities.name]}")
            }
            else {
                println("${it[Users.name]} lives nowhere")
            }
        }

        println("Functions and group by:")

        ((Cities innerJoin Users).slice(Cities.name, Users.id.count()).selectAll().groupBy(Cities.name)).forEach {
            val cityName = it[Cities.name]
            val userCount = it[Users.id.count()]

            if (userCount > 0) {
                println("$userCount user(s) live(s) in $cityName")
            } else {
                println("Nobody lives in $cityName")
            }
        }

        SchemaUtils.drop (Users, Cities)
    */
}
