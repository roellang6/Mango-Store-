package dev.ran.MangoStore.View
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.ran.MangoStore.Adapter.FavoriteAdapter
import dev.ran.MangoStore.Api.OffDB
import dev.ran.MangoStore.Model.OffDBlist
import dev.ran.MangoStore.R
import kotlinx.android.synthetic.main.activity_favorite.*
import java.util.ArrayList

class Favorite : AppCompatActivity() {
    private lateinit var rvdisplayfav: RecyclerView
    private lateinit var swipelayoutfav: SwipeRefreshLayout
    private lateinit var addfavorites: TextView
    private lateinit var pbloadingfav: ProgressBar
    private lateinit var lyEmpty: LinearLayout

    private lateinit var favAdapter: FavoriteAdapter
    private var favlist = ArrayList<OffDBlist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        rvdisplayfav = findViewById(R.id.rvfavoritelist)
        addfavorites = findViewById(R.id.addfavid)
        swipelayoutfav = findViewById(R.id.swipeLayoutid)

        lyEmpty = findViewById(R.id.lyempty)

        pbloadingfav = findViewById(R.id.pb_fav)

        addfavorites.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        rvdisplayfav.layoutManager = GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false)

        favid?.setOnClickListener {  }

        swipelayoutfav.setOnRefreshListener { checkFavorite() }

        backIndex?.setOnClickListener { backtoIndex() }

        addfavorites.setOnClickListener { backtoIndex()}

        checkFavorite()
    }

    //Check Favorite if Empty or not and display
    private fun checkFavorite(){
        favlist.clear()
        rvdisplayfav.adapter = null

        swipelayoutfav.isRefreshing = false

        val db = OffDB(this)
        val dataFavorite = db.getDataMango()
        val favId = Array(dataFavorite.size){"0"}
        val trackName = Array(dataFavorite.size){"null"}
        val artistName = Array(dataFavorite.size){"null"}
        val genre = Array(dataFavorite.size){"null"}
        val trackPrice = Array(dataFavorite.size){"null"}
        val urlpic = Array(dataFavorite.size){"null"}

        if(dataFavorite.isEmpty()){
            rvdisplayfav.isVisible = false
            lyEmpty.isVisible = true
            lyEmpty.alpha = 0f
            lyEmpty.animate().setDuration(1000).alpha(1f).withEndAction {
                pbloadingfav.isVisible = false
            }
        }else{
            rvdisplayfav.isVisible = true
            rvdisplayfav.alpha = 0f
            rvdisplayfav.animate().setDuration(1000).alpha(1f).withEndAction {
                var index = 0
                for(favitem in dataFavorite){
                    favId[index] = favitem.mid.toString()
                    trackName[index] = favitem.trackName
                    artistName[index] = favitem.artistName
                    genre[index] = favitem.primaryGenreName
                    trackPrice[index] = favitem.trackPrice
                    urlpic[index] = favitem.urlPicture

                    favlist.add(OffDBlist(Integer.parseInt(favId[index]),trackName[index], artistName[index],genre[index], trackPrice[index], urlpic[index]))

                    index ++
                }
                favAdapter = FavoriteAdapter(this, favlist)
                rvdisplayfav.adapter = favAdapter

                pbloadingfav.isVisible = false
            }
        }
    }

    //onBackPressed back to Index
    private fun backtoIndex(){
        startActivity(Intent(this, Index::class.java))
        finish()
    }

    override fun onBackPressed() {
        backtoIndex()
    }
}