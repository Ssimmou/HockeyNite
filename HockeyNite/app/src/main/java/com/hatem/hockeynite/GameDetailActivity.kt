package com.hatem.hockeynite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hatem.hockeynite.Communication.GameDetailService
import com.hatem.hockeynite.Models.DetailGame
import kotlinx.android.synthetic.main.activity_game_detail.*
import kotlinx.android.synthetic.main.activity_game_detail.item_detail_container
import kotlinx.android.synthetic.main.activity_game_detail.swipelist

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [GameListActivity].
 */
class GameDetailActivity : AppCompatActivity(),
                            GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{


    var gDetector: GestureDetectorCompat?=null
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var gamedetail: ArrayList<DetailGame>
    private var detService: Intent?= null

    var gameID= 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        setSupportActionBar(detail_toolbar)



        this.gDetector= GestureDetectorCompat(this,this)
        this.gDetector?.setOnDoubleTapListener(this)

        GameDetailFragment.ARG_GAME_ID

        //Refresh
        swipelist.setOnRefreshListener{
            refreshAction() // refresh your list contents somehow
            swipelist.isRefreshing = false
        }

        // get reference to button
        val parier: Button = findViewById(R.id.parier)
        // set on-click listener
        parier.setOnClickListener {
            val intent = Intent(this, PariActivity::class.java).apply {
                putExtra(PariActivity.GAME_ID, 1)
                //putExtra("Equipe1",gamedetail[0].team1Name)
                //putExtra("Equipe2",gamedetail[0].team2Name)
            }
           // intent.getStringExtra("gameID")
            startActivity(intent)
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        broadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?,intent:Intent) {
                if (intent.getSerializableExtra(GameDetailService.DET_MESSAGE) !=null)
                    gamedetail= intent.getSerializableExtra(GameDetailService.DET_MESSAGE) as ArrayList<DetailGame>

                //updateData(gamedetail)
            }

        }





        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        GameDetailFragment.ARG_GAME_ID,
                        intent.getStringExtra(GameDetailFragment.ARG_GAME_ID)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()
        }

    }


    override fun onStart() {
        super.onStart()
        detService=Intent(applicationContext, GameDetailService::class.java)
        startService(detService)

        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(GameDetailService.DET_RESULT)
        )

    }

    override fun onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        stopService(detService)

        super.onStop()

    }

    override fun onRestart() {
        super.onRestart()
        stopService(detService)


    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
            IntentFilter(GameDetailService.DET_RESULT)
        )


        super.onResume()

        // Affichage du statut d'avant-plan par Toast
        Toast.makeText(this@GameDetailActivity,"HockeyNite - onResume",Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)

        // Affichage du statut d'avant-plan par Toast
        Toast.makeText(this@GameDetailActivity,"HockeyNite - onPause",Toast.LENGTH_SHORT).show()
        super.onPause()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        stopService(detService)

        super.onDestroy()
    }


    fun updateData(){

        stopService(detService)

        detService=Intent(applicationContext, GameDetailService::class.java)
        startService(detService)
    }
    private fun refreshAction() {
        ConstraintLayout.setBackgroundColor(Color.RED)
        //updateData()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.gDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {

                NavUtils.navigateUpTo(this, Intent(this, GameListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    override fun onDown(e: MotionEvent?): Boolean {
        ConstraintLayout.setBackgroundColor(Color.RED)
        return true

    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        ConstraintLayout.setBackgroundColor(Color.GRAY)
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        item_detail_container.setBackgroundColor(Color.BLUE)
        return true
    }




    override fun onLongPress(e: MotionEvent?) {
        ConstraintLayout.setBackgroundColor(Color.RED)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        ConstraintLayout.setBackgroundColor(Color.GRAY)
        return true
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        ConstraintLayout.setBackgroundColor(Color.GREEN)
        return true

    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        ConstraintLayout.setBackgroundColor(Color.MAGENTA)
        return true

    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        ConstraintLayout.setBackgroundColor(Color.YELLOW)
        return true

    }

    override fun onShowPress(e: MotionEvent?) {
        ConstraintLayout.setBackgroundColor(Color.CYAN)

    }
}
