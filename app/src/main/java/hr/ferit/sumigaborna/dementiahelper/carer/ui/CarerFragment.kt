package hr.ferit.sumigaborna.dementiahelper.carer.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.view_holder.baseTileHolder
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.carer.data.CarerUI
import hr.ferit.sumigaborna.dementiahelper.carer.view_model.CarerVM
import hr.ferit.sumigaborna.dementiahelper.notes.list.ui.NotesListFragment
import hr.ferit.sumigaborna.dementiahelper.reminder.ui.ReminderFragment
import hr.ferit.sumigaborna.dementiahelper.todo.ui.TodoFragment
import kotlinx.android.synthetic.main.fragment_carer.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CarerFragment: BaseFragment(),
    CarerFragmentListener {

    override fun getLayout(): Int = R.layout.fragment_carer
    private val controller by inject<CarerFragmentController>{ parametersOf(this) }
    private val carerViewModel by viewModel<CarerVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        carerViewModel.carerLiveData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
        })
        carerViewModel.carerFailureData.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initEpoxy()
    }

    private fun initUI() {
        carerViewModel.getCarerPassword()
        buttonConfirmButton.setOnClickListener {
            if(etPassword.text!!.isNotBlank() && carerViewModel.carerPassword.isNotBlank() && etPassword.text.toString() == carerViewModel.carerPassword){
                clPassword.visibility = View.GONE
                rvCarer.visibility = View.VISIBLE
                etPassword.text!!.clear()
            }
            else{
                toastMessage("Wrong carer password!")
                clPassword.visibility = View.VISIBLE
                rvCarer.visibility = View.GONE
            }
        }
    }

    private fun initEpoxy() {
        val layoutManager = GridLayoutManager(this.context,2,RecyclerView.VERTICAL,false)
        controller.spanCount = 2
        layoutManager.spanSizeLookup = controller.spanSizeLookup
        rvCarer.layoutManager = layoutManager
        rvCarer.setController(controller)
    }

    override fun openTile(id: Int) {
        when(id){
            0-> activity!!.showFragment(R.id.fragmentContainer,TodoFragment(),true)
            1-> activity!!.showFragment(R.id.fragmentContainer,NotesListFragment(),true)
            2-> activity!!.showFragment(R.id.fragmentContainer,
                ReminderFragment(),true)
            3->{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:911")
                startActivity(intent)
            }
        }
    }
}

interface CarerFragmentListener{
    fun openTile(id:Int)
}

class CarerFragmentController(private val listener: CarerFragmentListener):TypedEpoxyController<CarerUI>(){
    override fun buildModels(data: CarerUI) {
        data.carerTiles.forEach {
            baseTileHolder {
                id(it.id)
                tileIcon(it.tileIcon)
                tileTitle(it.tileTitle)
                tileListener { model, parentView, clickedView, position ->
                    listener.openTile(it.id)
                }
            }
        }
    }
}