package com.hatem.hockeynite

import Client
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hatem.hockeynite.Communication.Communication
import com.hatem.hockeynite.Models.DetailGame
import com.hatem.hockeynite.Models.Games

import kotlinx.android.synthetic.main.game_list_content.view.*
import kotlinx.android.synthetic.main.game_list.*
import kotlinx.android.synthetic.main.game_list.item_detail_container
import kotlinx.android.synthetic.main.game_list.swipelist

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [GameDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class GameListActivity : AppCompatActivity() {
    var matchList= ArrayList<Games>()
    var listeDetailsParties= ArrayList<DetailGame>()
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private val adresseIP= null
    private val progressbarr: ProgressBar?= null
    private var comService: Intent?= null
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        swipelist?.setOnRefreshListener{
            refreshAction()
            swipelist?.isRefreshing = false
        }
        //setSupportActionBar(AppBarLayout)
        //toolbar.title = title
        //val tabs: TabLayout = findViewById(R.id.tabs)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
        setupRecyclerView(item_list)

        broadcastReceiver = object:BroadcastReceiver() {
            override fun onReceive(context: Context?,intent:Intent) {
                matchList = intent.getSerializableExtra(Communication.COM_MESSAGE) as ArrayList<Games>
               // listeDetailsParties= intent.getSerializableExtra(Communication.COM_MESSAGE_DETAIL) as ArrayList<DetailGame>
                updateData(matchList,listeDetailsParties)

            }

        }






    }




     override fun onStart() {
        super.onStart()
         comService=Intent(applicationContext, Communication::class.java)
         startService(comService)

        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(Communication.COM_RESULT)
        )

    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()
        stopService(comService)
    }

    override fun onRestart() {
        super.onRestart()

        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(Communication.COM_RESULT))


    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(Communication.COM_RESULT)
        )
        super.onResume()
        setupRecyclerView(item_list)

    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)

        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun refreshAction() {
        swipelist?.setBackgroundColor(Color.GRAY)
        updateList()
        setupRecyclerView(item_list)
    }

    private fun updateList() {
        stopService(comService)
        comService=Intent(applicationContext, Communication::class.java)
        startService(comService)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        //recyclerView.adapter = GameRecyclerViewAdapter(this,matchList, twoPane)

        val recyclerView = item_list
        val manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = GameRecyclerViewAdapter(this,matchList,
    //        listeDetailsParties,
            twoPane)
        recyclerView.setAdapter( recyclerView.adapter)
    }

    fun updateData(listeParties:ArrayList<Games>,listdetail:ArrayList<DetailGame>) {
        //progressBar.setVisibility(View.INVISIBLE)
        if (listeParties == null)
        {
            Toast.makeText(this, "Liste non-disponible", Toast.LENGTH_SHORT).show()
            return
        }
        setupRecyclerView(item_list)

    }


    class GameRecyclerViewAdapter(
        private val parentActivity: GameListActivity,
        private val values: ArrayList<Games>,
       // private val valuesDetail: ArrayList<DetailGame>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<GameRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val Game = v.tag as Games
                if (twoPane) {
                    val fragment = GameDetailFragment().apply {
                        arguments = Bundle().apply {
                            putInt(GameDetailFragment.ARG_GAME_ID, Game.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, GameDetailActivity::class.java).apply {
                        putExtra(GameDetailFragment.ARG_GAME_ID, Game.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.game_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val gameID = values[position]
            //val gameDetailGame= valuesDetail[gameID.id]


/*
            holder.score1.text=gameDetailGame.team1Goals.toString()
            holder.score2.text=gameDetailGame.team2Goals.toString()
            holder.NomEquipe1.text=gameDetailGame.team1Name
            holder.NomEquipe2.text=gameDetailGame.team2Name
            holder.periode.text = gameDetailGame.isEnded.toString()
*/

            holder.score1.text = gameID.team1Id.toString()
            holder.score2.text = gameID.team2Id.toString()
            holder.NomEquipe1.text = setNomEquipe(gameID.team1Id)
            holder.NomEquipe2.text = setNomEquipe(gameID.team2Id)
            holder.periode.text = setPeriode(gameID.ended,gameID)


            with(holder.itemView) {
                tag = gameID
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val score1: TextView = view.Score_equipe1
            val score2: TextView = view.Score_equipe2
            val NomEquipe1: TextView = view.Nom_equipe1
            val NomEquipe2: TextView = view.Nom_equipe2
            val periode: TextView = view.periode


        }
        fun setNomEquipe(idEquipe: Int?): String{
            if(idEquipe== 1)
                return "barca"
            else if (idEquipe==8)
                return "Getafe"
            else if (idEquipe==3)
                return "Real Madrid"
            else if (idEquipe==7)
                return "Valdolid"
            else if (idEquipe==6)
                return "Mallorca"
            else if (idEquipe==9)
                return "Atletico"
            else
                return "equipe"
        }
        fun setPeriode(periode: Int?,game: Games ): String{

            if(periode==1)
                return "FIN"
            else if(periode==0)
                return game.date.toDate().toString()
           else
                return ""
        }
    }




}
