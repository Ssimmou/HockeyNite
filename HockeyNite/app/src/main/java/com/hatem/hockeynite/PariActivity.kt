package com.hatem.hockeynite

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_pari_game.*

class PariActivity : AppCompatActivity() {

    var message: String?=null
    var choosedTeaamID: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pari_game)

        val gameID:String? = intent.getStringExtra("GameID")
        val parierbut: Button= findViewById(R.id.parier)

        Equipe1.setOnClickListener {

            if(choosedTeaamID==null)
                choosedTeaamID= "1"          //a changer
            else{
                choosedTeaamID="1"
                Equipe2.setBackgroundColor(Color.TRANSPARENT)
             }


            Equipe1.setBackgroundColor(Color.RED)
        }

        Equipe2.setOnClickListener {

            if(choosedTeaamID==null)
                choosedTeaamID= "2"          //a changer
            else{
                choosedTeaamID="2"
                Equipe1.setBackgroundColor(Color.TRANSPARENT)
            }


            Equipe2.setBackgroundColor(Color.RED)
        }

        parierbut.setOnClickListener {
            validerPari(choosedTeaamID)
        }


    }

    private fun validerPari(choosedTeaamID: String?) {
        if(true)
            message= "merci"+ choosedTeaamID.toString()

        InformePari(message)
    }

    fun InformePari(message: String?){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
/*
    fun onSNACK(view: View){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, "Replace with your own action",
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.LTGRAY)
        val textView =
            snackbarView.findViewById(android.support.design.R.id.) as TextView
        textView.setTextColor(Color.BLUE)
        textView.textSize = 28f
        snackbar.show()
    }*/
}