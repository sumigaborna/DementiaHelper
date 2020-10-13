package hr.ferit.sumigaborna.dementiahelper.reaction_game.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.airbnb.epoxy.TypedEpoxyController
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import hr.ferit.sumigaborna.dementiahelper.reaction_game.game.ReactionGameFragment
import hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model.ReactionGameUI
import hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model.ReactionGameVM
import kotlinx.android.synthetic.main.fragment_reaction_game_list.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReactionGameListFragment : BaseFragment(){

    override fun getLayout(): Int = R.layout.fragment_reaction_game_list
    private val viewModel by viewModel<ReactionGameVM>()
    private val controller by inject<ReactionGameListController> { parametersOf(this) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.reactionGameData.observe(viewLifecycleOwner, Observer {
            controller.setData(it)
            tvAllTimeAverageReactionTime.text = it.allTimeAverageReactionTime.toString()
            tvAllTimeAverageScore.text = it.allTimeAverageScore.toString()
                if(this.isResumed) hideProgressBar(pbReactionGameList)
        })
        viewModel.reactionGameUpload.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
        viewModel.reactionGameGet.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){
        viewModel.getReports()
        showProgressBar(pbReactionGameList)
        rvReactionGameList.setController(controller)
        fbNewReactionGame.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(KEY_REACTION_GAME_NEW_ID,viewModel.reactionGameData.value!!.listResult.size.toString())
            activity!!.showFragment(R.id.fragmentContainer,ReactionGameFragment(),true,bundle)
        }
    }
}

class ReactionGameListController:TypedEpoxyController<ReactionGameUI>(){
    override fun buildModels(data: ReactionGameUI) {
        data.listResult.forEach {
            reactionGameItemHolder {
                id(it.id)
                allTimeAverage(data.allTimeAverageReactionTime.toString())
                allTimeScoreAverage(data.allTimeAverageScore.toString())
                dateAndDay(it.dateAndDay)
                averageTime(it.averageReactionTime)
                score(it.score.toString())
            }
        }

    }
}