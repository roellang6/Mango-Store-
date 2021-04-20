package dev.ran.MangoStore
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import dev.ran.MangoStore.Api.NetworkCheck
import dev.ran.MangoStore.View.Index
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var networkCheck = NetworkCheck()
    private var chnetwork = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chnetwork = networkCheck.networkisConnected(this)
        checkConnected()

    }
    //Check Network Connected
    private fun checkConnected(){
        if(chnetwork){
            idlogo.isVisible = true
            idlogo.alpha = 0f
            idlogo.animate().setDuration(1000).alpha(1f).withEndAction {
                startActivity(Intent(this, Index::class.java))
                finish()
            }
        }else{
            lynetwork1.isVisible = true
            lynetwork1.alpha = 0f
            lynetwork1.animate().setDuration(1000).alpha(1f).withEndAction {
                idlogo.isVisible = false
            }
        }
    }
}