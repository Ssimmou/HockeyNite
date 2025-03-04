
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.GsonBuilder

import com.google.gson.internal.LinkedTreeMap
import com.hatem.hockeynite.Models.Bets
import com.hatem.hockeynite.Models.DetailGame
import com.hatem.hockeynite.Models.Games
import com.hatem.hockeynite.Models.Result

import org.joda.time.DateTime


import java.util.*
import kotlin.collections.ArrayList
import java.net.*
import java.io.DataOutputStream
import java.io.DataInputStream
import java.io.PrintWriter






class Client {



    private val tcpServeurPort = 1248
    private val TIMEOUT = 5000
    private val MAX_TENTATIVE = 5

    private var aSocket: DatagramSocket? = null
    private var adress: InetAddress? = null
    private var serveurPort: Int = 0
    private var clientPort: Int = 0
    private var WaitingMessage: Thread? = null

    //private val MutexLock = Any()

    private var error = false
    private var tentative = 0

    fun setServeur(adress: InetAddress, serveurPort: Int, clientPort: Int) {
        this.adress = adress
        this.serveurPort = serveurPort
        this.clientPort = clientPort
    }

    fun getListGames(): ArrayList<Games>? {
        aSocket = DatagramSocket(this.clientPort)

        val ask = this.adress?.let { Request().craftGetMatchList(it, this.serveurPort) }
        val stream = ask?.let { serialize(it) }
        var datagram = ask?.destinationPort?.let {
            stream?.size?.let { it1 ->
                DatagramPacket(
                    stream,
                    it1,
                    ask.destination,
                    it
                )
            }
        }
        aSocket!!.send(datagram) // emission non-bloquante

        val buffer = ByteArray(4000)
        datagram = DatagramPacket(buffer, buffer.size)
        println("haha")
        aSocket!!.receive(datagram)
        var reply = unSerializeReply(datagram)

        var gameList: ArrayList<LinkedTreeMap<String, Int>> = reply.value as ArrayList<LinkedTreeMap<String, Int>>;
        var List: ArrayList<Games>? = null
        var i: Int = 0
        for (i in 0..gameList.size - 1) {
            var id = gameList.get(i).get("id") as Int
            var team1Id = gameList.get(i).get("team1Id") as Int
            var team2Id = gameList.get(i).get("team2Id") as Int
            var date = DateTime.parse(gameList.get(i).get("date") as String)
            var ended= gameList.get(i).get("ended") as Int

            val game: Games = Games(id, team1Id,team1Id,date,ended)

            if (List != null) {
                List.add(game)
            }
            println(Integer.toString(i + 1) + " - " + team1Id.toInt() + " vs " + team2Id.toInt() + " at " + date.toDate().hours + ":" + date.toDate().minutes + " = = " + date.toDate().timezoneOffset )
        }

        aSocket!!.close()
/*
        println("Entrer le code de match a voir en details : (0 pour quitter )");
        val br = BufferedReader(InputStreamReader(System.`in`))
        var choix = Integer.parseInt(br.readLine())
        if (choix > 0 && choix <= gameList.size) {
            var team1Id = gameList.get(choix - 1).get("id") as Double
            displayMatchDetails(team1Id.toInt())
        }
        if (error) {
            println("-- Erreur Serveur TimeOut --")
        }

 */
        return List
    }

    private fun displayMatchDetails(id: Int) {
        var choix = -1
        //var matchList: ListMatchName? = null
        var aHost: InetAddress? = null
        val serveurPort = 6780
        val clientPort = 6779
        val commObject = Client()

        try {
            aHost = InetAddress.getByName("localhost")
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }

        //Set server port and host
        if (aHost != null) {
            commObject.setServeur(aHost, serveurPort, clientPort)
        }

        println("Recuperation de la liste des matchs, veuillez patienter")

        commObject.getDetailsGame(id)

    }

    private fun getDetailsGame(id: Int): DetailGame?{
        aSocket = DatagramSocket(this.clientPort)
        val ask = this.adress?.let { Request().craftGetMatchDetail(it, this.serveurPort, id) }

        val stream = ask?.let { serialize(it) }
        var datagram = ask?.destinationPort?.let {
            stream?.size?.let { it1 ->
                DatagramPacket(
                    stream,
                    it1,
                    ask.destination,
                    it
                )
            }
        }
        aSocket!!.send(datagram) // emission non-bloquante

        val buffer = ByteArray(4000)
        datagram = DatagramPacket(buffer, buffer.size)
        aSocket!!.receive(datagram)

        var reply = unSerializeReply(datagram)
        var detail : LinkedTreeMap<String, Object> = reply.value as LinkedTreeMap<String, Object>;

        val DetailGame: DetailGame= DetailGame(detail.get("team1Name") as String, detail.get("team2Name") as String, DateTime.parse(detail.get("date") as String), (detail.get("team1Goals") as Double).toInt(),(detail.get("team2Goals") as Double).toInt(),(detail.get("team1Penalties") as Double).toInt(),(detail.get("team2Penalties") as Double).toInt(),(detail.get("isEnded") as Double).toInt() )


        println("Teams : \t\t" + detail.get("team1Name") as String + " " + detail.get("team2Name") as String)
        println("Goals : \t\t" + (detail.get("team1Goals") as Double).toInt() + "-" + (detail.get("team2Goals") as Double).toInt())
        println("Penalties : \t\t" + (detail.get("team1Penalties") as Double).toInt() + "-" + (detail.get("team2Penalties") as Double).toInt())

/*
        println("You want to bet ? ")
        println("0 = draw")
        println("1 = home team to win")
        println("2 = away team to win")
        println("3 = quit")

        val sc = Scanner(System.`in`)
        var choix = sc.nextInt()
        if(choix > 2)
            return null;
        println("The amount ? ")
        var amount = sc.nextFloat()
        aSocket!!.close()

        play(id, choix, amount)

 */
        return DetailGame
    }

    private fun play(idGame: Int, choice: Int, bet: Float) {
        var bet = Bets(0, idGame, choice, bet)
        val sClient = Socket("localhost", tcpServeurPort)

        var `is` = DataInputStream(sClient.getInputStream())
        var os = DataOutputStream(sClient.getOutputStream())

        val gson = GsonBuilder().setPrettyPrinting().create()
        val str = gson.toJson(bet) as String

        val pw = PrintWriter(os)

        pw.println(str)
        pw.flush()
        var sc = Scanner(`is`)
        val result = sc.nextInt()


        while(true) {
            var updates = ""
            while (sc.hasNext()) {

                var str = sc.nextLine() //We wait for the object
                updates += str
                str = str.trim()
                if (str == "}")
                    break
            }
            println(updates)
            var gameDetail: DetailGame = gson.fromJson(updates, DetailGame::class.java)
            println("Teams : \t\t" + gameDetail.team1Name as String + " " + gameDetail.team2Name as String)
            println("Goals : \t\t" + gameDetail.team1Goals + "-" + gameDetail.team2Goals)
            println("Penalties : \t\t" + (gameDetail.team1Penalties) + "-" + (gameDetail.team2Penalties))

            println("ENDED = " + gameDetail.isEnded)
            if(gameDetail.isEnded == 1){
                var betRes = ""
                while (sc.hasNext()) {

                    var str = sc.nextLine() //We wait for the object
                    betRes += str
                    str = str.trim()
                    if (str == "}")
                        break
                    println(betRes)
                }
                val gson =  GsonBuilder().setPrettyPrinting().create()
                var res: Result
                res = gson.fromJson(betRes, Result::class.java)
                var gain : Double = 0.0
                if(res.res == bet.choice) {
                    gain = bet.bet / res.winningSum
                    gain = gain * res.sum
                }
                else
                    gain = 0.0
                println("wa = " + gain)
            }
        }
        sClient.close()
    }

}
