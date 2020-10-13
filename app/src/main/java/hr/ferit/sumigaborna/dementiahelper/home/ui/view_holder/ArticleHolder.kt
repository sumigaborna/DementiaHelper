package hr.ferit.sumigaborna.dementiahelper.home.ui.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.*
import com.bumptech.glide.Glide
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_article.view.*

@EpoxyModelClass(layout = R.layout.cell_article)
abstract class ArticleHolderModel : EpoxyModelWithHolder<ArticleHolder>(){
    @EpoxyAttribute lateinit var title :String
    @EpoxyAttribute lateinit var description :String
    @EpoxyAttribute lateinit var publishedAt :String
    @EpoxyAttribute var urlImage :String? = null
    @EpoxyAttribute(hash = false) lateinit var clickListener : View.OnClickListener
    override fun bind(holder: ArticleHolder) {
        super.bind(holder)
        holder.apply {
            titleView.text = title
            descriptionView.text = description
            publishedAtView.text = publishedAt
            if(!urlImage.isNullOrBlank()) Glide.with(holder.imageView).load(urlImage).into(holder.imageView)
            articleView.setOnClickListener(clickListener)
        }
    }
}

class ArticleHolder : EpoxyHolder(){
    lateinit var titleView : TextView
    lateinit var descriptionView : TextView
    lateinit var publishedAtView : TextView
    lateinit var imageView : ImageView
    lateinit var articleView : View
    override fun bindView(itemView: View) {
        titleView = itemView.tvArticleTitle
        descriptionView = itemView.tvArticleDescription
        publishedAtView = itemView.tvArticlePublishedAt
        imageView = itemView.ivArticleImage
        articleView = itemView
    }
}