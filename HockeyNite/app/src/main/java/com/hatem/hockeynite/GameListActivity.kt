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
import android.widget.ThemedSpinnerAdapter
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hatem.hockeynite.Communication.Communication
import com.hatem.hockeynite.Models.DetailGame
import com.hatem.hockeynite.Models.Games
import kotlinx.android.synthetic.main.activity_game_list.*

import kotlinx.android.synthetic.main.game_list_content.view.*
import kotlinx.android.synthetic.main.game_list.*
import kotlinx.android.synthetic.main.game_list.item_detail_container
import kotlinx.android.synthetic.main.game_list.swipelist
import kotlinx.coroutines.*
import java.lang.Runnable
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [GameDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class GameListActivity : AppCompatActivity() {
    val commObject = Client()
    var matchList= ArrayList<Games>()

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private val adresseIP= null
    private val progressbarr: ProgressBar?= null
    private val comService: Intent?= null
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


        broadcastReceiver = object:BroadcastReceiver() {
            override fun onReceive(context: Context?,intent:Intent) {
                matchList = intent.getSerializableExtra(Communication.COM_MESSAGE) as ArrayList<Games>
                updateData(matchList)
            }
        }

        val intent=Intent(applicationContext, Communication::class.java)
        startService(intent)

        setupRecyclerView(item_list)
    }


     override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(Communication.COM_RESULT)
        )
    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onStop()

    }

    private fun refreshAction() {
        swipelist?.setBackgroundColor(Color.GRAY)
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = GameRecyclerViewAdapter(this,matchList, twoPane)
    }

    fun updateData(listeParties:ArrayList<Games>) {
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
                            putInt(GameDetailFragment.ARG_ITEM_ID, Game.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, GameDetailActivity::class.java).apply {
                        putExtra(GameDetailFragment.ARG_ITEM_ID, Game.id)
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
            var game=parentActivity.commObject.getDetailsGame(gameID.id)


            holder.score1.text = game?.team1Goals.toString()
            holder.score2.text = game?.team2Goals.toString()
            holder.NomEquipe1.text = game?.team1Name
            holder.NomEquipe2.text = game?.team2Name
            holder.periode.text = game?.date.toString()
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
    }




}
