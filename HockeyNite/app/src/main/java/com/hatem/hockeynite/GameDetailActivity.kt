package com.hatem.hockeynite

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.GestureDetector
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_game_detail.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [GameListActivity].
 */
class GameDetailActivity : AppCompatActivity(),
                            GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{


    var gDetector: GestureDetectorCompat?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        setSupportActionBar(detail_toolbar)
        this.gDetector= GestureDetectorCompat(this,this)
        this.gDetector?.setOnDoubleTapListener(this)
        swipelist.setOnRefreshListener{
            refreshAction() // refresh your list contents somehow
            swipelist.isRefreshing = false
        }

        // get reference to button
        val parier: Button = findViewById(R.id.parier)
        // set on-click listener
        parier.setOnClickListener {
            val intent = Intent(this, PariActivity::class.java).apply {
                putExtra("GameID", "ssss")
            }
            startActivity(intent)
        }
        //btn_click_me.setOnClickListener {
        // your code to perform when the user clicks on the button
        // Toast.makeText(this, "You clicked me.", Toast.LENGTH_SHORT).show()


        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = GameDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        GameDetailFragment.ARG_ITEM_ID,
                        intent.getStringExtra(GameDetailFragment.ARG_ITEM_ID)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.item_detail_container, fragment)
                .commit()
        }

    }

    private fun refreshAction() {
        ConstraintLayout.setBackgroundColor(Color.RED)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.gDetector?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

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



    fun UpdateData(){
        item_detail_container.setBackgroundColor(Color.YELLOW)
    }



    override fun onLongPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onShowPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
