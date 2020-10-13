package hr.ferit.sumigaborna.dementiahelper.memory_lane.ui

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.common.KEY_IMAGE_ID
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.memory_lane.view_model.MemoryLaneVM
import kotlinx.android.synthetic.main.item_dialog_image.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleDialogFragment : DialogFragment(){

    private val viewModel by viewModel<MemoryLaneVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memoryLaneData.observe(viewLifecycleOwner, Observer { memoryLaneUI ->
            val storageReference = memoryLaneUI.images.find { it.imageId == arguments?.getString(KEY_IMAGE_ID)?.toInt() }?.imageURL
            Glide.with(this).load(storageReference).into(ivDialogImage)
            if(this.isResumed) hideProgressBar(pbImageDialog)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Dialog_Alert)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.item_dialog_image,container,false)

    override fun onStart() {
        super.onStart()
        val myDialog = dialog
        if(myDialog!=null){
            val width = ActionBar.LayoutParams.MATCH_PARENT
            val height = ActionBar.LayoutParams.MATCH_PARENT
            myDialog.window?.setLayout(width,height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        showProgressBar(pbImageDialog)
        viewModel.getImages()
        ivDialogImage.setOnClickListener {
            this.dismiss()
        }
    }
}
