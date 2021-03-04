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


    init {
        // // 8. Task: Add LiveData to the ScoreViewModel
        _score.value = finalScore
        Log.i("ScoreViewModel", "Final score is $finalScore")
    }
}
