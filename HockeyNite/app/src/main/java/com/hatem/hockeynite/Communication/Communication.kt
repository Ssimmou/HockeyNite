package com.hatem.hockeynite.Communication

import Client
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hatem.hockeynite.Models.Games
import com.hatem.hockeynite.R
import java.net.InetAddress

class Communication: Service() {
    // est-ce que le service
    // est en train de s’exécuter ?
    private var runFlag = false
    // thread séparé qui effectue la MAJ
    private var updater: Updater? = null
    private val broadcaster = LocalBroadcastManager.getInstance(this)
    override fun onBind(intent:Intent): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        // créer le fil de MAJ
        // à la création du service
        this.updater = Updater()
        Log.d(TAG, "onCreated")
    }
    fun sendResult(message: ArrayList<Games>?) {
        val intent = Intent(COM_RESULT)
        if (message != null)
            intent.putExtra(COM_MESSAGE, message)
        broadcaster.sendBroadcast(intent)
    }
    override fun onStartCommand(intent:Intent,
                                flags:Int,
                                startId:Int):Int {
        super.onStartCommand(intent, flags, startId)
        // démarrer le fil de MAJ
        // au démarrage du service
       // if (this.updater.isAlive()) this.updater.stop() //TODO don't work
        this.runFlag = true
        this.updater?.start()
        Log.d(TAG, "onStarted")
        return START_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        // arrêter et détruire le fil de MAJ
        // à la destruction du service
        // mettre updater à null pour le GC
        this.runFlag = false
        this.updater?.interrupt()
        //this.updater = null
        Log.d(TAG, "onDestroyed")
    }
    /**
     * Thread that performs the actual update from the online service
     */
    // note : AsynchTask pour les threads UI
    private inner class Updater:Thread("UpdaterService-Updater") {
        public override fun run() { // méthode invoquée pour démarrer le fil
            val comService = this@Communication // réf. Sur le service
            while (comService.runFlag)
            { // MAJ via les méthode onStartCOmmand et onDestroy
                Log.d(TAG, "Updater running")
                try
                {
                    /* Get DATA */
                    val commClient = Client()
                    val adr: InetAddress
                    try
                    {
                        adr = InetAddress.getByName(getApplication().getSharedPreferences(getResources().getString(
                            R.string.FileShared), Context.MODE_PRIVATE).getString(getResources().getString(R.string.Serveur_adresse), "192.168.1.1"))
                    }
                    catch (e:Exception) {
                        sendResult(null!!)
                        return
                    }
                    // Placer les paramètres de communications
                    commClient.setServeur(adr, 6780, 6779)
                    // Lecture de la liste des parties
                    val listeParties: ArrayList<Games>? = commClient.getListGames()
                    sendResult(listeParties)
                    Log.d(TAG, "Updater ran")
                    Thread.sleep(DELAY.toLong()) // s’endormir entre chaque mise à jour
                }
                catch (e:InterruptedException) {
                    // exception est déclenchée lorsqu’on signale interrupt()
                    comService.runFlag = false
                }
            }
        }
    }// donner un nom au thread à des fins de debug
    // Updater
    companion object {
        val TAG = "ComService"
        // intervalle entre les maj = 2 minutes
        internal val DELAY = 120000
        val COM_RESULT = "REQUEST_PROCESSED"
        val COM_MESSAGE = "COM_MSG"
    }

}