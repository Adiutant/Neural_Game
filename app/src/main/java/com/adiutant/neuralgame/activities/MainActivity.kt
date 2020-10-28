package com.adiutant.neuralgame .activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
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
    private var turn = 0L
    private var history:INDArray = Nd4j.create(floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f,0f), intArrayOf(1,9))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cross= findViewById(R.id.cross_button)
        rock= findViewById(R.id.rock_button)
        paper= findViewById(R.id.paper_button)
        userTurnView = findViewById(R.id.userTurnView)
        neuralTurnView = findViewById(R.id.neuralTurnView)
        //mPresenter = MainPresenter()
        neural = NeuralNetwork()
        onGameLoaded(neural)
        cross.setOnClickListener { history.putScalar(turn,0f)

            turn++
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(1f, 0f, 0f)))
            neural.predict(history)
            if (turn==6L)
            {
                turn =0L
            }
        }
        rock.setOnClickListener { history.putScalar(turn,1f)

            turn++
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(0f, 1f, 0f)))
            neural.predict(history)
            if (turn==6L)
            {
                turn =0L
            }
        }
        paper.setOnClickListener { history.putScalar(turn,2f)

            turn++
            onPredictionShowed()
            neural.fit(history, Nd4j.create(floatArrayOf(0f, 0f, 1f)))
            neural.predict(history)
            if (turn==6L)
            {
                turn =0L
            }
        }


    }

    override fun onGameLoaded(neural: NeuralNetwork){
        neural.initWeights()
       neural.predict(history)
    }

    override fun onPredictionShowed() {
        var max:Float = 0f
        var index:Long = 0L
        for (i in 0L until (3L))
        {
            if (neural.resultl2?.getFloat(i)!! >max)
            {
                max = neural.resultl2?.getFloat(i)!!
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
}