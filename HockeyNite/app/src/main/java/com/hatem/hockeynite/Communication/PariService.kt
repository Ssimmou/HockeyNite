package com.hatem.hockeynite.Communication

import Client
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hatem.hockeynite.Models.DetailGame
import java.net.InetAddress


class PariService: Service() {
    // est-ce que le service
    // est en train de s’exécuter ?
    private var runFlag = false
    // thread séparé qui effectue la MAJ
    private lateinit var Parieur: ThreadPari
    private lateinit var broadcaster: LocalBroadcastManager
    var gameID = 1
    var choix= 1
    var bet=15

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // créer le fil de MAJ
        // à la création du service
        broadcaster = LocalBroadcastManager.getInstance(this)
        this.Parieur = ThreadPari()
        Log.d(TAG,"onCreated")
    }

    fun sendResult(message: String?) {
        val intent = Intent(PARI_RESULT)
        if (message != null) {
            intent.putExtra(PARI_MESSAGE,message)
        }
        broadcaster.sendBroadcast(intent)
    }

    override fun onStartCommand(
        intent: Intent,
        flags: Int,
        startId: Int
    ): Int {
        super.onStartCommand(intent,flags,startId)
        // démarrer le fil de MAJ
        // au démarrage du service
        //if (runFlag == true)  //TODO don't work

        //Recuperer le gameID donné par l'activity de detail
        //gameID= intent.getIntExtra(ID_MATCH, this.gameID)
        this.runFlag = true
        if (this.Parieur.isAlive) {
            this.Parieur.interrupt()
        } else
            this.Parieur.start()

        Log.d(TAG,"onStarted")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // arrêter et détruire le fil de MAJ
        // à la destruction du service
        // mettre Parieur à null pour le GC
        this.runFlag = false
        this.Parieur.interrupt()
        //this.Parieur = null
        Log.d(TAG,"onDestroyed")
    }

    /**
     * Thread that performs the actual update from the online service
     */
    // note : AsynchTask pour les threads UI


    //TODO l'appli plante si on demande beaucoup de detail a cause que les client est deja en cours d'utilisation
    private inner class ThreadPari : Thread("ParieurService-Thread") {
        override fun run() { // méthode invoquée pour démarrer le fil

            Log.d(GameDetailService.TAG," Parieur running")
            try {
                /* Get DATA */
                val pariClient = Client()
                //val adr: InetAddress
                var aHost: InetAddress
                val serveurPort = 6780
                val clientPort = 6776

                try {
                    aHost = InetAddress.getByName("10.0.2.2")
                } catch (e: Exception) {
                    sendResult(null!!)
                    return
                }
                // Placer les paramètres de communications
                pariClient.setServeur(aHost,serveurPort,clientPort)
                // Lecture de la liste des parties
                //val listeParties: ArrayList<Games>? = commClient.getListGames()

                val message: String= pariClient.play(gameID,choix, bet.toFloat())

                sendResult(message)

                Log.d(GameDetailService.TAG,"Detail Updater ran")
                this.interrupt()

            } catch (e: InterruptedException) {
                // exception est déclenchée lorsqu’on signale interrupt()

            }

        }
    }

    companion object {
        val TAG = "PariService"
        // intervalle entre les maj = 2 minutes
        internal val DELAY = 120000
        val PARI_RESULT = "REQUEST_PROCESSED"
        val PARI_MESSAGE = "COM_MSG"
        val ID_MATCH = "game_id"
    }


}

