package hr.ferit.sumigaborna.dementiahelper.app.base.ui.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_base_tile.view.*

@EpoxyModelClass(layout = R.layout.cell_base_tile)
abstract class BaseTileHolderModel : EpoxyModelWithHolder<BaseHolder>(){
    @EpoxyAttribute var tileIcon : Int = 0
    @EpoxyAttribute lateinit var tileTitle : String
    @EpoxyAttribute(hash = false) lateinit var tileListener : View.OnClickListener
    override fun bind(holder: BaseHolder) {
        holder.apply {
            ivTile.setImageResource(tileIcon)
            tvTile.text = tileTitle
            view.setOnClickListener(tileListener)
        }
    }
}

class BaseHolder : EpoxyHolder(){
    lateinit var ivTile : ImageView
    lateinit var tvTile : TextView
    lateinit var view : View
    override fun bindView(itemView: View) {
        ivTile = itemView.ivTile
        tvTile = itemView.tvTile
        view = itemView
    }
}