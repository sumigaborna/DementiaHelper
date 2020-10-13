package hr.ferit.sumigaborna.dementiahelper.memory_lane.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.memory_lane.ui.view_model.imageItemModelHolder
import hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model.MemoryLaneUI
import hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model.MemoryLaneVM
import kotlinx.android.synthetic.main.fragment_memory_lane.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class MemoryLaneFragment : BaseFragment(),
    MemoryLaneFragmentListener {

    override fun getLayout(): Int = R.layout.fragment_memory_lane
    private val viewModel by viewModel<MemoryLaneVM>()
    private val controller by inject<MemoryLaneFragmentController>{ parametersOf(this)}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memoryLaneData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            if(this.isResumed)hideProgressBar(pbMemoryLane)
        })
        viewModel.uploadData.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
        viewModel.downloadData.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initEpoxy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data?.data ?: return
            viewModel.uploadImage(selectedImage)
        }
    }

    private fun initUI(){
        showProgressBar(pbMemoryLane)
        viewModel.getImages()
        fbGallery.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            galleryIntent.type = "image/*"
            startActivityForResult(
                Intent.createChooser(galleryIntent, "Select Picture"),
                REQUEST_CODE_GALLERY
            )
        }
    }

    private fun initEpoxy(){
        val layoutManager = GridLayoutManager(this.context,2, RecyclerView.VERTICAL,false)
        controller.spanCount = 2
        layoutManager.spanSizeLookup = controller.spanSizeLookup
        rvMemoryLane.layoutManager = layoutManager
        rvMemoryLane.setController(controller)
    }

    override fun openImage(imageId: Int) {
        val bundle = Bundle()
        bundle.putString(KEY_IMAGE_ID,imageId.toString())
        val singleDialogFragment =
            SingleDialogFragment()
        singleDialogFragment.arguments = bundle
        singleDialogFragment.show(childFragmentManager,"SingleDialogFragment")
    }

    override fun deleteImage(imageId: Int) = viewModel.deleteImage(imageId)

}

interface MemoryLaneFragmentListener{
    fun openImage(imageId: Int)
    fun deleteImage(imageId:Int)
}

class MemoryLaneFragmentController(private val listener : MemoryLaneFragmentListener) :
    TypedEpoxyController<MemoryLaneUI>() {
    override fun buildModels(data: MemoryLaneUI) {
        data.images.forEach {
            imageItemModelHolder {
                id(it.imageId)
                imageURL(it.imageURL)
                imageListener { model, parentView, clickedView, position ->
                    listener.openImage(it.imageId)
                }
                deleteImageListener { model, parentView, clickedView, position ->
                    listener.deleteImage(it.imageId)
                }
            }
        }
    }
}