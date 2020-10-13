package hr.ferit.sumigaborna.dementiahelper.reaction_game.game

import android.os.Bundle
import android.view.View
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model.ReactionGameResult
import hr.ferit.sumigaborna.dementiahelper.reaction_game.view_model.ReactionGameVM
import kotlinx.android.synthetic.main.fragment_reaction_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.app.common.*
import kotlin.random.Random

class ReactionGameFragment : BaseFragment(){

    override fun getLayout(): Int = R.layout.fragment_reaction_game
    private val viewModel by viewModel<ReactionGameVM>()
    private var turnCounter = 1
    private var correctCounter = 0
    private var numberToTap = Random.nextInt(1,9)
    private val listReactionTime = mutableListOf<Float>()
    private var report  = ReactionGameResult(0,0, mutableListOf(), EMPTY_STRING, EMPTY_STRING)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.reactionGameGet.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGame()
    }

    private fun initGame() {
        viewModel.getReports()
        tvReactionGameTries.text = "Turn: $turnCounter/10"
        val randomNumberList = listOf(1,2,3,4,5,6,7,8,9).shuffled()
        val circleList = listOf(tvCircle1,tvCircle2,tvCircle3,tvCircle4,tvCircle5,tvCircle6,tvCircle7,tvCircle8,tvCircle9)

        randomNumberList.forEachIndexed { index, i ->
            circleList[index].text = i.toString()
        }
        numberToTap = Random.nextInt(1,9)
        tvNumberToTap.text = numberToTap.toString()
        val startTime = System.currentTimeMillis()
        circleList.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                val endTime = System.currentTimeMillis()
                listReactionTime.add((endTime-startTime)/1000f)
                if(textView.text.toString().toInt()==numberToTap){
                    correctCounter++
                    if(turnCounter<10)startNextTurn()
                    else finishGame()
                }
                else{
                    if(turnCounter<10)startNextTurn()
                    else finishGame()
                }
            }
        }
    }

    private fun startNextTurn(){
        turnCounter++
        initGame()
    }

    private fun finishGame(){
        toastMessageLong("Game finished, you scored: $correctCounter/10")
        report.id = arguments!!.getString(KEY_REACTION_GAME_NEW_ID,"KEY_REACTION_GAME_NEW_ID").toInt()
        report.score = correctCounter
        report.reactionTime = listReactionTime
        var reactionTimeSum = 0f
        listReactionTime.forEach { reactionTimeSum +=it }
        report.averageReactionTime = "${reactionTimeSum/10f} seconds"
        viewModel.uploadGameReport(report)
        activity!!.onBackPressed()
    }
}