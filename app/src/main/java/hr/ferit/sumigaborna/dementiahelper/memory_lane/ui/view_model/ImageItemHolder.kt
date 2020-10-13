package hr.ferit.sumigaborna.dementiahelper.memory_lane.ui.view_model

import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_image_item.view.*


@EpoxyModelClass(layout = R.layout.cell_image_item)
abstract class ImageItemModelHolder : EpoxyModelWithHolder<ImageItemHolder>() {
    @EpoxyAttribute
    lateinit var imageURL: StorageReference
    @EpoxyAttribute(hash=false)
    lateinit var imageListener: View.OnClickListener
    @EpoxyAttribute(hash=false)
    lateinit var deleteImageListener: View.OnClickListener

    override fun bind(holder: ImageItemHolder) {
        Glide.with(holder.ivImage).load(imageURL).centerCrop().into(holder.ivImage)
        holder.ivImage.setOnClickListener(imageListener)
        holder.ivDeleteImage.setOnClickListener(deleteImageListener)
    }
}

class ImageItemHolder : EpoxyHolder() {
    lateinit var ivImage : ImageView
    lateinit var ivDeleteImage : ImageView
    override fun bindView(itemView: View) {
        ivImage = itemView.ivImage
        ivDeleteImage = itemView.ivDeleteImage
    }
}