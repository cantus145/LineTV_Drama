package example.com.linetvtestdramaapp.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import example.com.linetvtestdramaapp.R

/**
 * 戲劇ViewHolder
 */
class DramaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imgDrama: ImageView = itemView.findViewById(R.id.imgDrama)

    var txvDramaName: TextView = itemView.findViewById(R.id.txvDramaName)
    var txvDramaRating: TextView = itemView.findViewById(R.id.txvDramaRating)
    var txvDramaCreated: TextView =itemView.findViewById(R.id.txvDramaCreated)
}