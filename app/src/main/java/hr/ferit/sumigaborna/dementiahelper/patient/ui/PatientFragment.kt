package hr.ferit.sumigaborna.dementiahelper.patient.ui

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
import hr.ferit.sumigaborna.dementiahelper.app.common.hasNetwork
import hr.ferit.sumigaborna.dementiahelper.app.common.hideProgressBar
import hr.ferit.sumigaborna.dementiahelper.app.common.showFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.showProgressBar
import hr.ferit.sumigaborna.dementiahelper.memory_game.ui.MemoryGameFragment
import hr.ferit.sumigaborna.dementiahelper.memory_game.ui.MemoryGameListFragment
import hr.ferit.sumigaborna.dementiahelper.memory_lane.ui.MemoryLaneFragment
import hr.ferit.sumigaborna.dementiahelper.notes.list.ui.NotesListFragment
import hr.ferit.sumigaborna.dementiahelper.patient.ui.view_holders.quotesHolder
import hr.ferit.sumigaborna.dementiahelper.patient.ui.view_holders.weatherHolder
import hr.ferit.sumigaborna.dementiahelper.patient.view_model.PatientUI
import hr.ferit.sumigaborna.dementiahelper.patient.view_model.PatientVM
import hr.ferit.sumigaborna.dementiahelper.reaction_game.list.ReactionGameListFragment
import hr.ferit.sumigaborna.dementiahelper.todo.ui.TodoFragment
import kotlinx.android.synthetic.main.fragment_patient.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.random.Random


class PatientFragment : BaseFragment(),
    PatientFragmentListener {

    override fun getLayout(): Int = R.layout.fragment_patient
    private val controller by inject<PatientFragmentController>{ parametersOf(this) }
    private val patientViewModel by viewModel<PatientVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        patientViewModel.patientLiveData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            rvPatient.scrollToPosition(0)
            if(it.loadingDone) hideProgressBar(pbPatient)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        showProgressBar(pbPatient)
        initEpoxy()
        if(hasNetwork(activity!!)) patientViewModel.getPatientCityWeatherAndQuotes()
        else hideProgressBar(pbPatient)
    }

    private fun initEpoxy() {
        val layoutManager = GridLayoutManager(this.context,2, RecyclerView.VERTICAL,false)
        controller.spanCount = 2
        layoutManager.spanSizeLookup = controller.spanSizeLookup
        rvPatient.layoutManager = layoutManager
        rvPatient.setController(controller)
    }

    override fun openTile(id: Int) {
        when(id){
            0-> activity!!.showFragment(R.id.fragmentContainer,
                MemoryLaneFragment(),true)
            1-> activity!!.showFragment(R.id.fragmentContainer,TodoFragment(),true)
            2-> activity!!.showFragment(R.id.fragmentContainer,NotesListFragment(),true)
            3->{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:911")
                startActivity(intent)
            }
            4-> activity!!.showFragment(R.id.fragmentContainer,
                ReactionGameListFragment(),true)
            5-> activity!!.showFragment(R.id.fragmentContainer,
                MemoryGameListFragment(),true)
        }
    }
}

interface PatientFragmentListener{
    fun openTile(id:Int)
}

class PatientFragmentController(private val listener: PatientFragmentListener):
    TypedEpoxyController<PatientUI>(){
    override fun buildModels(data: PatientUI) {
        if(data.weatherInfo.id!=0)
        weatherHolder {
            id(data.weatherInfo.id)
            day(data.weatherInfo.day)
            date(data.weatherInfo.date)
            city(data.weatherInfo.city)
            icon(data.weatherInfo.iconURL)
            temperature(data.weatherInfo.temp)
            weatherDescription(data.weatherInfo.weatherDescription)
            spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
        }
        if(data.quotesList.quotes.isNotEmpty())
        quotesHolder {
            id(data.quotesList.id)
            val random = Random.nextInt(0,data.quotesList.quotes.size)
            if(data.quotesList.quotes[random].author != "Null")
            quote(data.quotesList.quotes[random].text+" - "+data.quotesList.quotes[random].author)
            else quote(data.quotesList.quotes[random].text+" - Unknown")
            spanSizeOverride { totalSpanCount, position, itemCount -> 2 }
        }
        data.patientTiles.forEach {
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