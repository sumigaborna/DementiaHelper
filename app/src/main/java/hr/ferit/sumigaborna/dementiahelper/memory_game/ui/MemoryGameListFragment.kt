package hr.ferit.sumigaborna.dementiahelper.memory_game.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.memory_game.MemoryGameUI
import hr.ferit.sumigaborna.dementiahelper.memory_game.MemoryGameVM
import hr.ferit.sumigaborna.dementiahelper.memory_game.ui.view_holders.memoryGameItemHolder
import kotlinx.android.synthetic.main.fragment_memory_game_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MemoryGameListFragment : BaseFragment(){

    override fun getLayout(): Int = R.layout.fragment_memory_game_list
    private val viewModel by viewModel<MemoryGameVM>()
    private val controller by inject<MemoryGameListController> { parametersOf(this) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memoryGameData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            tvAllTimeAverageReactionTime.text = it.allTimeAverageReactionTime.toString()
            tvAllTimeAverageScore.text = it.allTimeAverageScore.toInt().toString()
            if(this.isResumed) hideProgressBar(pbMemoryGameList)
        })
        viewModel.memoryGameUpload.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
        viewModel.memoryGameGet.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){
        viewModel.getReports()
        showProgressBar(pbMemoryGameList)
        rvMemoryGameList.setController(controller)
        fbNewMemoryGame.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(KEY_MEMORY_GAME_NEW_ID,viewModel.memoryGameData.value!!.listResult.size.toString())
            activity!!.showFragment(R.id.fragmentContainer, MemoryGameFragment(),true,bundle)
        }
    }
}

class MemoryGameListController: TypedEpoxyController<MemoryGameUI>(){
    override fun buildModels(data: MemoryGameUI) {
        data.listResult.forEach {
            memoryGameItemHolder {
                id(it.id)
                allTimeAverage(data.allTimeAverageReactionTime.toString())
                allTimeScoreAverage(data.allTimeAverageScore.toString())
                dateAndDay(it.dateAndDay)
                averageTime(it.averageMemoryTime)
                score(it.score.toString())
            }
        }

    }
}