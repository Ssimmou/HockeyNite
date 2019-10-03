import Models.Games

import com.google.gson.internal.LinkedTreeMap
import java.io.BufferedReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


import java.io.InputStreamReader
import java.net.UnknownHostException
import kotlin.collections.ArrayList


class Communication {

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

        var i: Int = 0
        for (i in 0..gameList.size - 1) {
            var team1Id = gameList.get(i).get("team1Id") as Double
            var team2Id = gameList.get(i).get("team2Id") as Double
            println(Integer.toString(i + 1) + " - " + team1Id.toInt() + " vs " + team2Id.toInt())
        }

        aSocket!!.close()

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
        return null
    }

    private fun displayMatchDetails(id: Int) {
        var choix = -1
        //var matchList: ListMatchName? = null
        var aHost: InetAddress? = null
        val serveurPort = 6780
        val clientPort = 6779
        val commObject = Communication()

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

    private fun getDetailsGame(id: Int) {
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
        println("Teams : \t\t" + detail.get("team1Name") as String + " " + detail.get("team2Name") as String)
        println("Goals : \t\t" + (detail.get("team1Goals") as Double).toInt() + "-" + (detail.get("team2Goals") as Double).toInt())
        println("Penalties : \t\t" + (detail.get("team1Penalties") as Double).toInt() + "-" + (detail.get("team2Penalties") as Double).toInt())

    }
}
