package hr.ferit.sumigaborna.dementiahelper.memory_game.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import hr.ferit.sumigaborna.dementiahelper.R
import hr.ferit.sumigaborna.dementiahelper.app.base.ui.BaseFragment
import hr.ferit.sumigaborna.dementiahelper.app.common.EMPTY_STRING
import hr.ferit.sumigaborna.dementiahelper.app.common.KEY_MEMORY_GAME_NEW_ID
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessage
import hr.ferit.sumigaborna.dementiahelper.app.common.toastMessageLong
import hr.ferit.sumigaborna.dementiahelper.memory_game.MemoryGameResult
import hr.ferit.sumigaborna.dementiahelper.memory_game.MemoryGameVM
import kotlinx.android.synthetic.main.fragment_memory_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.random.Random

class MemoryGameFragment :BaseFragment(){

    override fun getLayout(): Int = R.layout.fragment_memory_game
    private val viewModel by viewModel<MemoryGameVM>()
    private var turnCounter = 1
    private var correctCounter = 0
    private var tapCounter = 0
    private lateinit var squareList : MutableList<ImageView>
    private var randomNumberToGuess = Random.nextInt(3,6)
    private var randomSquares = listOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).shuffled().take(randomNumberToGuess)
    private var report =
        MemoryGameResult(
            0,
            0,
            mutableListOf(),
            EMPTY_STRING,
            EMPTY_STRING
        )
    private var startTime : Long = 0
    private var endTime : Long = 0
    private val listReactionTime = mutableListOf<Float>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.memoryGameGet.observe(viewLifecycleOwner, Observer {
            if(it.isNotBlank()) toastMessage(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReports()
        initGame()
    }

    private fun initGame(){
        tvMemoryGameTries.text = "Turn: $turnCounter/10"
        squareList = mutableListOf(ivSquare1,ivSquare2,ivSquare3,ivSquare4,
            ivSquare5,ivSquare6,ivSquare7,ivSquare8,
            ivSquare9,ivSquare10,ivSquare11,ivSquare12,
            ivSquare13,ivSquare14,ivSquare15,ivSquare16)
        randomNumberToGuess = Random.nextInt(3,6)
        randomSquares = listOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).shuffled().take(randomNumberToGuess)
        squareList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener { /*nothing*/ }
            randomSquares.forEach {
                if(index==it) imageView.setColorFilter(Color.BLUE)
            }
        }
        setTimerForChangingColor()
    }

    private fun continueGame(){
        startTime = System.currentTimeMillis()
        squareList.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if(randomSquares.find { it==index } != null){
                    tapCounter++
                    imageView.setColorFilter(Color.GREEN)
                    if(tapCounter==randomNumberToGuess){
                        finishTurn(true)
                    }
                }
                else finishTurn(false)
            }
        }
    }

    private fun setTimerForChangingColor() = Timer().schedule(object : TimerTask() {override fun run() {updateUI(squareList,randomSquares)}}, 2000)

    private fun updateUI(squareList: List<ImageView>, randomSquares: List<Int>){
        Handler(Looper.getMainLooper()).post { changeSquareColor(squareList,randomSquares) };
    }

    private fun changeSquareColor(squareList: List<ImageView>, randomSquares: List<Int>) {
        squareList.forEachIndexed { index, imageView ->
            randomSquares.forEach {
                if(index==it) imageView.setColorFilter(android.R.color.darker_gray)
            }
        }
        continueGame()
    }

    private fun finishTurn(isCorrect:Boolean){
        endTime = System.currentTimeMillis()
        turnCounter++
        tapCounter = 0
        if(isCorrect) {
            correctCounter +=1
        }
        squareList.forEachIndexed { index, imageView ->
            imageView.setColorFilter(android.R.color.darker_gray)
        }
        listReactionTime.add((endTime-startTime)/1000f)
        startTime=0
        endTime=0
        if(turnCounter>=10) finishGame()
        else initGame()
    }

    private fun finishGame(){
        report.id = arguments!!.getString(KEY_MEMORY_GAME_NEW_ID,"KEY_MEMORY_GAME_NEW_ID").toInt()
        report.score = correctCounter
        report.memoryTime = listReactionTime
        var reactionTimeSum = 0f
        listReactionTime.forEach { reactionTimeSum +=it }
        report.averageMemoryTime = "${reactionTimeSum/10f} seconds"
        viewModel.uploadMemoryGameResult(report)
        toastMessageLong("You scored $correctCounter/10")
        activity!!.onBackPressed()
    }
}