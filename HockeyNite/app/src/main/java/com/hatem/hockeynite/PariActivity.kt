package com.hatem.hockeynite

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pari_game.*

class PariActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pari_game)

        val st:String = intent.getStringExtra("GameID")
        val sss: TextView= findViewById(R.id.ssssss)
        sss.text=st
    }


}