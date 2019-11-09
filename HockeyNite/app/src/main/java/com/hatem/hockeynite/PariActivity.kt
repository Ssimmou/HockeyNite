package com.hatem.hockeynite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.Snackbar
import com.hatem.hockeynite.Communication.GameDetailService
import com.hatem.hockeynite.Communication.PariService
import com.hatem.hockeynite.Models.DetailGame
import kotlinx.android.synthetic.main.activity_pari_game.*

class PariActivity : AppCompatActivity() {

    var message: String?=null
    var choosedTeaamID: String?=null
    val gameID= 1
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var pariService: Intent?= null
    var msg=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pari_game)


        //val NomEquipe1= intent.getStringExtra("Equipe1")
        //val NomEquipe2= intent.getStringExtra("Equipe2")
        val idEquipe1= 1
        val idEquipe2=3
        val parierbut: Button= findViewById(R.id.parier)

        Equipe1.setOnClickListener {

            if(choosedTeaamID==null)
                choosedTeaamID= idEquipe1.toString()         //a changer
            else{
                choosedTeaamID=idEquipe1.toString()
                Equipe2.setBackgroundColor(Color.TRANSPARENT)
             }


            Equipe1.setBackgroundColor(Color.GREEN)
        }

        Equipe2.setOnClickListener {

            if(choosedTeaamID==null)
                choosedTeaamID= idEquipe2.toString()          //a changer
            else{
                choosedTeaamID= idEquipe2.toString()
                Equipe1.setBackgroundColor(Color.TRANSPARENT)
            }


            Equipe2.setBackgroundColor(Color.GREEN)
        }

        parierbut.setOnClickListener {
            validerPari(choosedTeaamID)
        }

        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?,intent:Intent) {
                if (intent.getStringExtra(PariService.PARI_MESSAGE) !=null)
                    msg= intent.getStringExtra(PariService.PARI_MESSAGE)

                validerPari(msg)
            }

        }

        pariService=Intent(applicationContext, PariService::class.java)
        startService(pariService)
    }
    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(PariService.PARI_RESULT)
        )
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(PariService.PARI_RESULT)
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onPause()
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun parier(idGame: Int, choice: Int, bet: Float){

    }
    private fun validerPari(choosedTeaamID: String?) {
        if(true)
            message= " Vous avez parié sur l'équipe "+ choosedTeaamID.toString()


        InformePari(message)
    }

    fun InformePari(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val GAME_ID = "item_id"
    }
}