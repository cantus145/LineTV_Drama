package example.com.linetvtestdramaapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import example.com.linetvtestdramaapp.DramaInfoActivity
import example.com.linetvtestdramaapp.R
import example.com.linetvtestdramaapp.config.AppConfig
import example.com.linetvtestdramaapp.serverApi.Data.Drama
import example.com.linetvtestdramaapp.tool.Util
import example.com.linetvtestdramaapp.viewHolder.DramaViewHolder

class DramaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 動畫(底部往上)
     */
    private var animResId: Int = R.anim.slide_in_from_bottom
    private var lastPosition = -1

    /**
     * 戲劇清單
     */
    var dramaList: MutableList<Drama> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dramaView = LayoutInflater.from(parent.context).inflate(
            R.layout.holder_drama, parent, false
        )
        return DramaViewHolder(dramaView)
    }

    override fun getItemCount(): Int {
        return dramaList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val context: Context = AppConfig.instance.getAppContext()

        //動畫效果
        lastPosition = Util.setAnimation(holder.itemView, animResId, position, lastPosition)

        val dramaHolder = holder as DramaViewHolder
        val drama: Drama = dramaList[position]
        Util.glideImgLoader(dramaHolder.imgDrama, drama.thumb)
        dramaHolder.txvDramaName.text = drama.name

        val dramaRating =
            context.resources.getString(R.string.rating) + ": " + drama.rating.toBigDecimal().toPlainString()
        val dramaCreated =
            context.resources.getString(R.string.create_date) + ": " + Util.dataTimeFormat(drama.created_at)
        
        dramaHolder.txvDramaRating.text = dramaRating
        dramaHolder.txvDramaCreated.text = dramaCreated

        dramaHolder.itemView.setOnClickListener {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(context, DramaInfoActivity::class.java)
            intent.putExtra("Drama", drama)
            context.startActivity(intent)
        }
    }

    fun addDramaList(list: MutableList<Drama>) {
        dramaList.addAll(list)
    }

    fun clearList() {
        dramaList.clear()
        lastPosition = -1
    }

}