package com.adiutant.neuralgame.activities

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.adiutant.neuralgame.R
import com.adiutant.neuralgame.mvp.models.NNetwork
import com.adiutant.neuralgame.mvp.models.NeuralNetwork
import com.adiutant.neuralgame.mvp.presenters.MainPresenter
import com.adiutant.neuralgame.mvp.views.MainView

class MainActivity : AppCompatActivity(), MainView{
    private lateinit var mPresenter:MainPresenter
    private lateinit  var neural: NeuralNetwork
    private lateinit var cross:ImageButton
    private lateinit var rock:ImageButton
    private lateinit var paper:ImageButton
    private lateinit var userTurnView:ImageView
    private lateinit var neuralTurnView:ImageView
    private var turn = 0
    private var history:ArrayList<Float> = arrayListOf(1f,2f,1f,3f,2f,2f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cross= findViewById(R.id.cross_button)
        rock= findViewById(R.id.rock_button)
        paper= findViewById(R.id.paper_button)
        userTurnView = findViewById(R.id.userTurnView)
        neuralTurnView = findViewById(R.id.neuralTurnView)
        mPresenter = MainPresenter()
        neural = NeuralNetwork()
        onGameLoaded(neural)
        cross.setOnClickListener { history[turn] = 1f

            turn++
            onPredictionShowed()
            neural.fit(history, arrayOf(1f,0f,0f))
            neural.predict(history)
            if (turn==6)
            {
                turn =0
            }
        }
        rock.setOnClickListener { history[turn] = 2f

            turn++
            onPredictionShowed()
            neural.fit(history, arrayOf(0f,1f,0f))
            neural.predict(history)
            if (turn==6)
            {
                turn =0
            }
        }
        paper.setOnClickListener { history[turn] = 3f

            turn++
            onPredictionShowed()
            neural.fit(history, arrayOf(0f,0f,1f))
            neural.predict(history)
            if (turn==6)
            {
                turn =0
            }
        }


    }

    override fun onGameLoaded(neural: NeuralNetwork){
        neural.initWeights()
        neural.predict(history)
    }

    override fun onPredictionShowed() {
        var max:Float = 0f
        var index:Int = 0
        for (i in 0 until (neural.resultl3?.size ?: 0))
        {
            if (neural.resultl3!![i]>max)
            {
                max = neural.resultl3!![i]
                index= i
            }
        }
        if (index == 0){neuralTurnView.setImageResource(R.drawable.rock)
        neuralTurnView.visibility = View.VISIBLE}
        if (index == 1){neuralTurnView.setImageResource(R.drawable.paper)
            neuralTurnView.visibility = View.VISIBLE}
        if (index == 2){neuralTurnView.setImageResource(R.drawable.cross)
            neuralTurnView.visibility = View.VISIBLE}

    }
}