package com.example.androidkotlinfundamentals.screens.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
// данные для ViewModel первый урок
//    // The current word
//    var word = ""
//    // The current score
//    var score = 0

    // The current word 2 LiveData Task: Add LiveData to the GameViewModel
//    val word = MutableLiveData<String>()

    // LiveData observers
    // The current word Add a backing property to score and word Encapsulate the LiveData
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score 2 LiveData Task: Add LiveData to the GameViewModel Encapsulate the LiveData
//    val score = MutableLiveData<Int>()

    // LiveData observers
    // The current score Add a backing property
    // Свойство _score теперь является изменяемой
    // версией игрового счета для внутреннего использования.
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Step 1: Use LiveData to detect a game-finished event
    // Event which triggers the end of the game
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    init {
        // для LiveData 2 Task: Add LiveData to the GameViewModel
//        word.value = ""

        // LiveData observers Encapsulate the LiveData Add a backing property
        _word.value = ""

        // LiveData 2 Task: Add LiveData to the GameViewModel
//        score.value = 0

        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = 0

        Log.i("GameViewModel","GameViewModel created!" )
        // в блоке данные сбрасываются когда создаем ViewModel а не фрагмент!
        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        // ViewModel
//        score--

        // LiveData Task: Add LiveData to the GameViewModel
//        score.value = (score.value)?.minus(1)

        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
//         // ViewModel
//        score++

        // LiveData Task: Add LiveData to the GameViewModel
//         score.value = (score.value)?.plus(1)

        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (!wordList.isEmpty())
        {
            // Step 1: Use LiveData to detect a game-finished event
            onGameFinish()

        } else {
            //Select and remove a word from the list
                // ViewModel
//            word = wordList.removeAt(0)

            // LiveData Task: Add LiveData to the GameViewModel
//            word.value = wordList.removeAt(0)

            // LiveData observers Encapsulate the LiveData Add a backing property
            _word.value = wordList.removeAt(0)
        }
    }

    // Step 1: Use LiveData to detect a game-finished event
    /** Method for the game completed event **/
    fun onGameFinish() {
//       _eventGameFinish.value = true
    }

// Step 2: Reset the game-finished event
    fun onGameFinishComplete() {
//        _eventGameFinish.value = false
    }





}