package dev.ran.MangoStore.View
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.like.LikeButton
import dev.ran.MangoStore.Adapter.IndexAdapter
import dev.ran.MangoStore.Api.NetworkCheck
import dev.ran.MangoStore.Model.DataGet
import dev.ran.MangoStore.R
import dev.ran.MangoStore.ViewModel.IndexViewModel
import kotlinx.android.synthetic.main.activity_index.*
import java.util.*

class Index : AppCompatActivity() {
    private lateinit var swipelayout: SwipeRefreshLayout
    private lateinit var favStar: LikeButton
    private lateinit var rvdisplay: RecyclerView
    private lateinit var tvnonet: TextView
    private lateinit var pbloading: ProgressBar
    private var networkCheck = NetworkCheck()
    private var chnetwork = false

    private lateinit var indexAdapter: IndexAdapter
    private var mlist = ArrayList<DataGet>()
    private lateinit var iviewModel: IndexViewModel
    private var dataSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        chnetwork = networkCheck.networkisConnected(this)

        pbloading = findViewById(R.id.pb_index)
        tvnonet = findViewById(R.id.noserver)

        rvdisplay = findViewById(R.id.rvlist)

        swipelayout = findViewById(R.id.idswipeLayout)
        favStar = findViewById(R.id.star_button)
        rvdisplay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        iviewModel = ViewModelProviders.of(this).get(IndexViewModel::class.java)
        iviewModel.refresh()
        
        swipelayout.setOnRefreshListener { checkConnected() }

        favStar.setOnClickListener {
            startActivity(Intent(this, Favorite::class.java))
            finish()
        }
        checkConnected()
    }

    //Check Network Connected
    private fun checkConnected() {
        mlist.clear()
        rvdisplay.adapter = null
        swipelayout.isRefreshing = false
        if (chnetwork) {
            rvlist.isVisible = true
            rvlist.alpha = 0f
            rvlist.animate().setDuration(1000).alpha(1f).withEndAction {
                indexDataViewModel()
            }
        } else {
            lynetwork.isVisible = true
            lynetwork.alpha = 0f
            lynetwork.animate().setDuration(1000).alpha(1f).withEndAction {
                rvlist.isVisible = false
                pbloading.isVisible = false

            }
        }
    }

    //Observe Data get from ViewModel
    private fun indexDataViewModel() {

        iviewModel.indexDataLive.observe(this, { indexdata ->

            dataSize = (iviewModel.indexDataLive.value?.size?.minus(1) ?: Int) as Int

            for (i in 0..dataSize) {
                iviewModel.indexDataLive.value?.let { mlist.add(it.get(i)) }
            }
            indexdata.let {
                rvdisplay.visibility = View.VISIBLE
                pbloading.visibility = View.GONE
                lynetwork.visibility = View.GONE
                tvnonet.visibility = View.GONE
            }

            indexAdapter = this.let { IndexAdapter(it, mlist) }
            rvdisplay.adapter = indexAdapter
            pbloading.isVisible = false
        })

        iviewModel.indexLoadError.observe(this, { loadError ->
            loadError?.let {
                tvnonet.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    rvdisplay.visibility = View.GONE
                    pbloading.isVisible = false
                }
            }
        })
    }
}