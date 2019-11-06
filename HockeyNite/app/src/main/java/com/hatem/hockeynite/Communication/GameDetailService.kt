package com.hatem.hockeynite.Communication

import com.hatem.hockeynite.Models.DetailGame

import Client
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.Serializable

import java.net.InetAddress

class GameDetailService: Service() {
    // est-ce que le service
    // est en train de s’exécuter ?
    private var runFlag = false
    // thread séparé qui effectue la MAJ
    private lateinit var updater: UpdaterDetail
    private lateinit var broadcaster: LocalBroadcastManager
    var gameID= 1


    override fun onBind(intent:Intent): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        // créer le fil de MAJ
        // à la création du service
        broadcaster= LocalBroadcastManager.getInstance(this)
        this.updater = UpdaterDetail()
        Log.d(TAG, "onCreated")
    }
    fun sendResult(message: ArrayList<DetailGame>?) {
        val intent = Intent(DET_RESULT)
        if (message != null) {
            intent.putExtra(DET_MESSAGE, message)
        }
        broadcaster.sendBroadcast(intent)
    }
    override fun onStartCommand(intent:Intent,
                                flags:Int,
                                startId:Int):Int {
        super.onStartCommand(intent, flags, startId)
        // démarrer le fil de MAJ
        // au démarrage du service
        //if (runFlag == true)  //TODO don't work

        //Recuperer le gameID donné par l'activity de detail
        //gameID= intent.getIntExtra(ID_MATCH, this.gameID)
        this.runFlag = true
        if(this.updater.isAlive) {
            this.updater.interrupt()
        }
        else
            this.updater.start()

        Log.d(TAG, "onStarted")
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        // arrêter et détruire le fil de MAJ
        // à la destruction du service
        // mettre updater à null pour le GC
        this.runFlag = false
        this.updater.interrupt()
        //this.updater = null
        Log.d(TAG, "onDestroyed")
    }
    /**
     * Thread that performs the actual update from the online service
     */
    // note : AsynchTask pour les threads UI


    //TODO l'appli plante si on demande beaucoup de detail a cause que les client est deja en cours d'utilisation
    private inner class UpdaterDetail:Thread("UpdaterDetailService-Updater") {
        override fun run() { // méthode invoquée pour démarrer le fil
            val desService = this@GameDetailService// réf. Sur le service
            if (desService.runFlag)
            { // MAJ via les méthode onStartCOmmand et onDestroy
            Log.d(GameDetailService.TAG, "Detail updater running")
            try
            {
                /* Get DATA */
                val detClient = Client()
                //val adr: InetAddress
                var aHost: InetAddress
                val serveurPort = 6780
                val clientPort = 6775



                try
                {
                    aHost = InetAddress.getByName("10.0.2.2")
                }
                catch (e:Exception) {
                    sendResult(null!!)
                    return
                }
                // Placer les paramètres de communications
                detClient.setServeur(aHost, serveurPort, clientPort)
                // Lecture de la liste des parties
                //val listeParties: ArrayList<Games>? = commClient.getListGames()


                //listeDetailsParties.add(commClient.getDetailsGame(listeParties[1].team1Id))
                //for (i in 0..listeParties!!.size) {
                val gamedetail: ArrayList<DetailGame>?= arrayListOf(detClient.getDetailsGame(gameID))

                //listeDetailsParties.add(commClient.getDetailsGame(3))
                //}

                sendResult(gamedetail)
                Log.d(GameDetailService.TAG, "Detail Updater ran")
                Thread.sleep(5000) // s’endormir entre chaque mise à jour
                this.interrupt()
            }
            catch (e:InterruptedException) {
                // exception est déclenchée lorsqu’on signale interrupt()
                desService.runFlag = false
            }
        }
        }
    }
    companion object {
        val TAG = "DetailService"
        // intervalle entre les maj = 2 minutes
        internal val DELAY = 120000
        val DET_RESULT = "REQUEST_PROCESSED"
        val DET_MESSAGE = "COM_MSG"
        val ID_MATCH = "game_id"
    }




}