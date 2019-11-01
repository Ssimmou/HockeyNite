package com.hatem.hockeynite

import Client
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.get
import com.hatem.hockeynite.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_game_detail.*
import kotlinx.android.synthetic.main.game_detail.*
import kotlinx.android.synthetic.main.game_detail.view.*
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [GameListActivity]
 * in two-pane mode (on tablets) or a [GameDetailActivity]
 * on handsets.
 */
class GameDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.id
            }
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
        var matchList = commObject.getListGames()


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.game_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.NomEquipe2.text= item?.id
            rootView.NomEquipe1.text= item?.id
            rootView.Score_equipe1_total.text= item?.id
            rootView.Score_equipe2_total.text= item?.id
            rootView.time.text= item?.id



        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
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
}
