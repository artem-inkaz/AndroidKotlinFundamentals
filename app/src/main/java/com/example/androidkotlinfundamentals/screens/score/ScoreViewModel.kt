package com.example.androidkotlinfundamentals.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {
    // The final score
//    var score = finalScore
// 8. Task: Add LiveData to the ScoreViewModel
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // 9. Task: Add the Play Again button
    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        // // 8. Task: Add LiveData to the ScoreViewModel
        _score.value = finalScore
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }
    // 9. Task: Add the Play Again button
    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }
    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }

}
