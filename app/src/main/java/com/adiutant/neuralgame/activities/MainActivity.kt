package com.adiutant.neuralgame .activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.adiutant.neuralgame.R
import com.adiutant.neuralgame.mvp.models.NeuralNetwork
import com.adiutant.neuralgame.mvp.presenters.MainPresenter
import com.adiutant.neuralgame.mvp.views.MainView
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class MainActivity : AppCompatActivity(), MainView{
    private lateinit var mPresenter:MainPresenter
    private lateinit  var neural: NeuralNetwork
    private lateinit var cross:ImageButton
    private lateinit var rock:ImageButton
    private lateinit var paper:ImageButton
    private lateinit var userTurnView:ImageView
    private lateinit var neuralTurnView:ImageView
    private var nScore= 0
    private var uScore=0
    private lateinit var score:TextView
    private var turn = 0L
    private var history:INDArray = Nd4j.create(floatArrayOf(0f,1f,0f,2f,1f,1f,0f,2f,0f,1f), intArrayOf(1,10))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cross= findViewById(R.id.cross_button)
        rock= findViewById(R.id.rock_button)
        paper= findViewById(R.id.paper_button)
        score = findViewById(R.id.score)
        userTurnView = findViewById(R.id.userTurnView)
        neuralTurnView = findViewById(R.id.neuralTurnView)
        //mPresenter = MainPresenter()
        neural = NeuralNetwork()
        onGameLoaded(neural)
        cross.setOnClickListener {
            neural.predict(history)
            onPlayerTurn(0f)
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(1f, 0f, 0f)))
            if (turn==history.columns().toLong())
            {
                for (i in 1 until history.columns())
                {
                    var temp = history.getFloat(1,i)
                    history.putScalar(i.toLong()-1,temp)


                }
                history.putScalar(history.columns().toLong()-1,0f)
            }
            else
            {
                history.putScalar(turn,0f)
                turn++
            }

        }
        rock.setOnClickListener {
            neural.predict(history)
            onPlayerTurn(1f)
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(0f, 1f, 0f)))
            if (turn==history.columns().toLong())
            {
                for (i in 1 until history.columns())
                {
                    var temp = history.getFloat(1,i)
                    history.putScalar(i.toLong()-1,temp)


                }
                history.putScalar(history.columns().toLong()-1,1f)
            }
            else
            {
                history.putScalar(turn,1f)
                turn++
            }

        }
        paper.setOnClickListener {
            neural.predict(history)
            onPlayerTurn(2f)
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(0f, 0f, 1f)))
            if (turn == history.columns().toLong()) {
                for (i in 1 until history.columns()) {
                    var temp = history.getFloat(1, i)
                    history.putScalar(i.toLong()-1,temp)


                }
                history.putScalar(history.columns().toLong()-1,2f)
            } else {
                history.putScalar(turn, 2f)
                turn++
            }

        }

    }

    override fun onGameLoaded(neural: NeuralNetwork){
        neural.initWeights()
       neural.predict(history)
    }

    override fun onPredictionShowed() {
        println(history)
        var max:Float = 0f
        var index:Long = 0L
        for (i in 0L until (3L))
        {
            if (neural.resultl3?.getFloat(i)!! >max)
            {
                max = neural.resultl3?.getFloat(i)!!
                index= i
            }
        }
        if (index == 0L){neuralTurnView.setImageResource(R.drawable.rock)
        neuralTurnView.visibility = View.VISIBLE}
        if (index == 1L){neuralTurnView.setImageResource(R.drawable.paper)
            neuralTurnView.visibility = View.VISIBLE}
        if (index == 2L){neuralTurnView.setImageResource(R.drawable.cross)
            neuralTurnView.visibility = View.VISIBLE}

    }

    override fun onPlayerTurn(turn: Float) {
        var max:Float = 0f
        var index:Long = 0L
        for (i in 0L until (3L))
        {
            if (neural.resultl3?.getFloat(i)!! >max)
            {
                max = neural.resultl3?.getFloat(i)!!
                index= i
            }
        }
        if ((index!=0L||turn!=1f)&&(index!=1L||turn!=2f)&&(index!=2L||turn!=0f)) {
            if (index == turn.toLong()) {
                nScore++
            } else {
                uScore++
            }
            score.text = "$uScore-$nScore"
        }
        if (turn==0f)
        {
            userTurnView.visibility = View.VISIBLE
            userTurnView.setImageResource(R.drawable.cross)
        }
        if (turn==1f)
        {
            userTurnView.visibility = View.VISIBLE
            userTurnView.setImageResource(R.drawable.rock)
        }
        if (turn==2f)
        {
            userTurnView.visibility = View.VISIBLE
            userTurnView.setImageResource(R.drawable.paper)
        }
    }
}