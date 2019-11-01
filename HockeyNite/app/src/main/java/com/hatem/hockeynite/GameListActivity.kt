package com.hatem.hockeynite

import Client
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.hatem.hockeynite.Models.Games

import com.hatem.hockeynite.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_game_detail.*
import kotlinx.android.synthetic.main.game_list_content.view.*
import kotlinx.android.synthetic.main.game_list.*
import kotlinx.android.synthetic.main.game_list.item_detail_container
import kotlinx.android.synthetic.main.game_list.swipelist
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

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private val adresseIP= null
    private val progressbarr: ProgressBar?= null
    private val comService: Intent?= null
    private val broadcastReceiver: BroadcastReceiver? =null
    private var twoPane: Boolean = false
    var matchList: ArrayList<Games>? = null

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



        var choix = -1
        //var matchList: ListMatchName? = null
        var aHost: InetAddress? = null
        val serveurPort = 6780
        val clientPort = 6779
        val commObject = Client()

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
         matchList = commObject.getListGames()


        setupRecyclerView(item_list)





    }

    private fun refreshAction() {
        swipelist?.setBackgroundColor(Color.GRAY)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, matchList, twoPane)
    }


    private fun displayMatchsList() {
        var choix = -1
        //var matchList: ListMatchName? = null
        var aHost: InetAddress? = null
        val serveurPort = 6780
        val clientPort = 6779
        val commObject = Client()

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

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: GameListActivity,
        private val values: ArrayList<Games>?,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = GameDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(GameDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, GameDetailActivity::class.java).apply {
                        putExtra(GameDetailFragment.ARG_ITEM_ID, item.id)
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
            val item = values?.get(position)

            holder.score1.text = item!!.team1Id.toString()
            holder.score2.text = item!!.team2Id.toString()
           // holder.NomEquipe1.text = item.content
            //holder.NomEquipe2.text = item.content
            holder.periode.text = "FIN"
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values!!.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val score1: TextView = view.Score_equipe1
            val score2: TextView = view.Score_equipe2
            val NomEquipe1: TextView = view.Nom_equipe1
            val NomEquipe2: TextView = view.Nom_equipe2
            val periode: TextView = view.periode

        }
    }




}
