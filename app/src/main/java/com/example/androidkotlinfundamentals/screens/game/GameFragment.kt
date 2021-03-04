/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.androidkotlinfundamentals.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.androidkotlinfundamentals.R
import com.example.androidkotlinfundamentals.databinding.GameFragmentBinding
import com.example.androidkotlinfundamentals.screens.title.TitleFragmentDirections

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {
//-------эти данные перенесли в GameViewModel
//    // The current word
//    private var word = ""
//    // The current score
//    private var score = 0
//    // The list of words - the front of the list is the next word to guess
//    private lateinit var wordList: MutableList<String>
    //--------------------------------------------
    // Reference with ViewModel
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
//        resetList() // перенесли в GameViewModel
//        nextWord() // перенесли в GameViewModel
        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        binding.endGameButton.setOnClickListener { onEndGame() }
        // initialization of the ViewModel
        Log.i("GameFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        // Live Data
        /** Setting up LiveData observation relationship **/
        // Attach observers to the LiveData objects
        viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
            binding.scoreText.text = newScore.toString()
        })
        /** Setting up LiveData observation relationship **/
        viewModel.word.observe(viewLifecycleOwner, Observer { newWord ->
            binding.wordText.text = newWord
        })
        // Step 1: Use LiveData to detect a game-finished event
//         Observer for the Game finished event
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer<Boolean> { hasFinished ->
            if (hasFinished) gameFinished()
        })

        // Удаляем т.к. не нужно после объявления Observer в onCreateView
//        updateScoreText()
//        updateWordText()
        return binding.root
    }

    /**
     * Resets the list of words and randomizes the order
     */
// перенесли в GameViewModel
//    private fun resetList() {
//        wordList = mutableListOf(
//                "queen",
//                "hospital",
//                "basketball",
//                "cat",
//                "change",
//                "snail",
//                "soup",
//                "calendar",
//                "sad",
//                "desk",
//                "guitar",
//                "home",
//                "railway",
//                "zebra",
//                "jelly",
//                "car",
//                "crow",
//                "trade",
//                "bag",
//                "roll",
//                "bubble"
//        )
//        wordList.shuffle()
//    }

    /** Methods for buttons presses **/
// первоначальный код перенесли в GameViewModel
//    private fun onSkip() {
//        score--
//        nextWord()
//    }
//
//    private fun onCorrect() {
//        score++
//        nextWord()
//    }

    private fun onSkip() {
        viewModel.onSkip()
        // Удаляем т.к. не нужно после объявления Observer в onCreateView
        //         Attach observers to the LiveData objects
//        updateWordText()
//        updateScoreText()
    }
    private fun onCorrect() {
        viewModel.onCorrect()
        // Удаляем т.к. не нужно после объявления Observer в onCreateView
        // Attach observers to the LiveData objects Encapsulate the LiveData Add a backing property
//        updateScoreText()
//        updateWordText()
    }

    /**
     * Moves to the next word in the list
     */
// первоначальный код перенесли в GameViewModel
//    private fun nextWord() {
//        if (!wordList.isEmpty()) {
//            //Select and remove a word from the list
//            word = wordList.removeAt(0)
//        }
//        updateWordText()
//        updateScoreText()
//    }

    /** Methods for updating the UI **/
// первоначальный код до перенесения в GameViewModel
//    private fun updateWordText() {
//        binding.wordText.text = word
//    }
//
//    private fun updateScoreText() {
//        binding.scoreText.text = score.toString()
//    }
    private fun updateWordText() {
        // ViewModel
//        binding.wordText.text = viewModel.word

        // LiveData Task: Add LiveData to the GameViewModel
        binding.wordText.text = viewModel.word.value
    }

    private fun updateScoreText() {
        // ViewModel
//        binding.scoreText.text = viewModel.score.toString()

        // LiveData Task: Add LiveData to the GameViewModel
        binding.scoreText.text = viewModel.score.value.toString()

    }

    private fun onEndGame() {
        gameFinished()
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        Toast.makeText(activity, "Game has just finished", Toast.LENGTH_SHORT).show()
        // ViewModel
//       val action = GameFragmentDirections.actionGameToScore(viewModel.score)

//Step 1: Use LiveData to detect a game-finished event
//        // LiveData Task: Add LiveData to the GameViewModel
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value?:0)
        findNavController().navigate(action)

// Step 2: Reset the game-finished event
        viewModel.onGameFinishComplete()
    }
}
