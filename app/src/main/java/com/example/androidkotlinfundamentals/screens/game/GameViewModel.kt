package com.example.androidkotlinfundamentals.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val timer: CountDownTimer

    // Add a timer
    companion object {

        // Time when the game is over
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Total time for the game
        private const val COUNTDOWN_TIME = 60000L

    }

    // Countdown time
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    // The String version of the current time
    // added android:text="@{gameViewModel.currentTimeString}" in game_fragment to timer_text
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }



    // LiveData observers
    // The current word Add a backing property to score and word Encapsulate the LiveData
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

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
        // LiveData observers Encapsulate the LiveData Add a backing property
        _word.value = ""
        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = 0

        Log.i("GameViewModel","GameViewModel created!" )
        // в блоке данные сбрасываются когда создаем ViewModel а не фрагмент!
        resetList()
        nextWord()

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
        }

        timer.start()

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
                // Cancel the timer чтобы избежать утечки памяти
                timer.cancel()
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        // LiveData observers Encapsulate the LiveData Add a backing property
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        if (wordList.isEmpty())
        {
            // Step 1: Use LiveData to detect a game-finished event
                // перенсли в  override fun onFinish() {}
//            onGameFinish()
            resetList()

        } else {
            // LiveData observers Encapsulate the LiveData Add a backing property
            _word.value = wordList.removeAt(0)
        }
    }

    // Step 1: Use LiveData to detect a game-finished event
    /** Method for the game completed event **/
    fun onGameFinish() {
       _eventGameFinish.value = true
    }

// Step 2: Reset the game-finished event
    fun onGameFinishComplete() {
       _eventGameFinish.value = false
    }




}