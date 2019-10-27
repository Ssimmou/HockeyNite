package com.hatem.hockeynite

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
        // get reference to button
        /*val btn_click_me: Button = findViewById(R.id.parier)
        // set on-click listener
        btn_click_me.setOnClickListener {
            val intent = Intent(this, PariActivity::class.java).apply {
                putExtra("GameID", "ssss")
            }
            startActivity(intent)
        }*/
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title =item?.content
            }
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.game_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.NomEquipe2.text= "sss"
            rootView.NomEquipe1.text= "aaaa"
            rootView.score1.text= "dddd"
            rootView.score2.text= "ffff"
            rootView.time.text= "15511515"



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
}
