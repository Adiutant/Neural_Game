package com.adiutant.neuralgame.mvp.views

import com.adiutant.neuralgame.mvp.models.NNetwork
import com.adiutant.neuralgame.mvp.models.NeuralNetwork

interface MainView {
    fun onGameLoaded(neural: NeuralNetwork)
    fun onPredictionShowed()
   // fun updateView()

    //fun onSearchResult(notes: List<Notes>)

    //fun onAllNotesDeleted()

    //fun onNoteDeleted()
    //fun openNoteScreen(noteId:Long)
}