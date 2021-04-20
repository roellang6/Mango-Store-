package dev.ran.MangoStore.Adapter
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import dev.ran.MangoStore.Api.OffDB
import dev.ran.MangoStore.Model.*
import dev.ran.MangoStore.R
import dev.ran.MangoStore.Util.*
import kotlinx.android.synthetic.main.indexlayout.view.*
import kotlinx.android.synthetic.main.savetofavorite.*
import kotlinx.android.synthetic.main.viewdatamango.*

class IndexAdapter(var context: Context, var dlistAll: ArrayList<DataGet>) :
    RecyclerView.Adapter<IndexAdapter.DataHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.indexlayout, parent, false
        )
    )
    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.bind(dlistAll.get(position))
        val viewItem = dlistAll.get(position)
        val sprice = viewItem.trackPrice.toString() + " " + viewItem.currency
        val pdViemango = getProgressDrawable(context)

        //Dialog for Viewing Content
        val cardItem = Dialog(context)
        cardItem.requestWindowFeature(Window.FEATURE_NO_TITLE)
        cardItem.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        cardItem.setContentView(R.layout.viewdatamango)
        val carddes = cardItem.findViewById<TextView>(R.id.idDes)
        val cardmview = cardItem.findViewById<TextView>(R.id.idmore)

        val db = OffDB(context)
        holder.cardItem.setOnClickListener {
            cardItem.imgprofv.loadImage(viewItem.artworkUrl100, pdViemango)
            cardItem.idartistname.text = viewItem.artistName
            cardItem.idGenre.text = "Genre : " + viewItem.primaryGenreName
            cardItem.idreleasedDate.text = "Released : " + viewItem.releaseDate

            //Track name if Empty
            val strackname = viewItem.trackName

            if (strackname != null) {
                cardItem.idtrackname.text = viewItem.trackName
            } else {
                cardItem.idtrackname.setText(R.string.defaulttxt)
            }

            //Description short and long if Empty
            val sshortDes = viewItem.shortDescription
            val slongDes = viewItem.longDescription

            if (sshortDes == null) {
                if (slongDes == null) {
                    carddes.isVisible = false
                    cardmview.isVisible = false
                } else {
                    cardItem.idDes.text = viewItem.longDescription
                    cardmview.isVisible = false
                }
            } else {
                cardItem.idDes.text = viewItem.shortDescription
                cardmview.isVisible = true
            }

            cardmview.setOnClickListener {
                if (cardmview.text == "view more") {
                    carddes.text = viewItem.longDescription
                    cardmview.setText(R.string.lesss)
                } else {
                    carddes.text = viewItem.shortDescription
                    cardmview.setText(R.string.mores)
                }
            }
            cardItem.show()
        }

        //Dialog to Save to Favorites or Not
        val dialogitem = Dialog(context)
        dialogitem.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogitem.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogitem.setContentView(R.layout.savetofavorite)

        val dataFavorite = db.getDataMango()
        val favtrackName = Array(dataFavorite.size) { "null" }
        val favartistName = Array(dataFavorite.size) { "null" }
        val favgenre = Array(dataFavorite.size) { "null" }

        dialogitem.namefav.text = viewItem.primaryGenreName
        val tracknameN: String
        val artisnameN = "" + viewItem.artistName
        val genreN = "" + viewItem.primaryGenreName
        val urlpictureN = "" + viewItem.artworkUrl100

        if (viewItem.trackName != null) {
            tracknameN = viewItem.trackName
        } else {
            tracknameN = "Unspecify (Unabridged)"
        }

        var index = 0
        var checker = 0

        for (favitem in dataFavorite) {
            favtrackName[index] = favitem.trackName
            favartistName[index] = favitem.artistName
            favgenre[index] = favitem.primaryGenreName

            if (tracknameN == favtrackName[index] && artisnameN == favartistName[index] && genreN == favgenre[index]) {
                checker = 1
            }
            index++
        }

        //Favorite star onclick
        holder.favstar.setOnClickListener {
            if(checker == 1){
                Toast.makeText(context, "This item already saved to favorites!", Toast.LENGTH_LONG).show()
            }else{
                dialogitem.show()
            }
            //Yes Button = Add to Favorites local database
            dialogitem.btnYes?.setOnClickListener {

                val long = db.addMango(OffDBlist(null, tracknameN, artisnameN, genreN, sprice, urlpictureN))
                if (long > -1) {
                    Toast.makeText(context, "Saved to favorites!", Toast.LENGTH_LONG).show()
                    holder.favstar.isLiked = true
                } else {
                    Toast.makeText(context, "Not Saved to favorites!", Toast.LENGTH_LONG).show()
                }
                dialogitem.cancel()
            }

            //No Button = cancel close dialog
            dialogitem.btnNo?.setOnClickListener {
                dialogitem.cancel()
            }
        }
    }
    override fun getItemCount(): Int {
        return dlistAll.size
    }

    class DataHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imgArtwork = v.imgprof
        val tvName = v.idname
        val tvPrice = v.idprize
        val cardItem = v.cardid
        val favstar = v.star_button
        val progressDrawable = getProgressDrawable(v.context)

        fun bind(dlist: DataGet) {
            tvName.text = dlist.primaryGenreName
            imgArtwork.loadImage(dlist.artworkUrl100, progressDrawable)
            val setprice = dlist.trackPrice.toString() + " " + dlist.currency
            tvPrice.text = setprice
        }
    }
}
