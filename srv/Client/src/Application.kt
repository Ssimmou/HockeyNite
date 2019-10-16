import java.awt.Menu
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicLong




object UDPClient {

    private val betServerPort = 1248
    private val betCounter = AtomicLong()


    @JvmStatic
    fun main(args: Array<String>) {
        var choix = -1

            println("Veuillez selectionner l'option souhaité:")
            println(" -- ")
            println(" 0 - back")
            println(" 1 - Affichage de la liste de matchs")
            println(" -- ")
            println("Faites votre choix ?")

            do {
                val br = BufferedReader(InputStreamReader(System.`in`))

                try {
                    choix = Integer.parseInt(br.readLine())
                } catch (nfe: NumberFormatException) {
                    System.err.println("Invalid Format!")
                    choix = -1
                } catch (e: IOException) {
                    e.printStackTrace()
                    choix = -1
                }

            } while (choix == -1)
            if (choix == 1) {
                displayMatchsList()
            }




        println("Bye :3")
    }

    private fun displayMatchsList() {
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
        var matchList = commObject.getListGames()

    }



}