package hr.ferit.sumigaborna.dementiahelper.todo.ui.view_holders

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_category.view.*

@EpoxyModelClass(layout = R.layout.cell_category)
abstract class CategoryHolderModel : EpoxyModelWithHolder<CategoryHolder>(){
    @EpoxyAttribute lateinit var categoryTitle : String
    override fun bind(holder: CategoryHolder) {
        holder.tvCategoryTitle.text = categoryTitle
    }
}

class CategoryHolder : EpoxyHolder(){
    lateinit var tvCategoryTitle : TextView
    override fun bindView(itemView: View) {
        tvCategoryTitle = itemView.tvCategoryTitle
    }
}