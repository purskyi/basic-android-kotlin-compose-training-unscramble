package com.example.unscramble.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

     var userGuess by mutableStateOf("")
         private set

    var userScore by mutableStateOf(0)
        private set

    var wordCount by mutableStateOf(0)
        private set



    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambleWord = pickRandomWordAndShuffle())

    }
    fun hacks():String{
        return currentWord
    }


    fun updateUserGuess(guessWord: String){
       userGuess = guessWord
    }




    fun checkUserGuess(){
        if (userGuess.equals(currentWord, ignoreCase = true)){
       updateGameState(SCORE_INCREASE)
        }
         else{
             _uiState.update { currentState ->
                 currentState.copy(isGuessedWordWrong = true )
             }
         }
        updateUserGuess("")
    }

    fun skip(){
        updateGameState()

        updateUserGuess("")
    }


    private fun updateGameState(addScore: Int = 0){
        if (usedWords.size == MAX_NO_OF_WORDS){
            //LastRound
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = currentState.score,
                    isGameOver = true,
                    wordCount = currentState.wordCount.inc()

                )
            }
        }
        else{
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentScrambleWord = pickRandomWordAndShuffle(),
                score = currentState.score.plus(addScore),
                wordCount = currentState.wordCount.inc()
            )
        }
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
         if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        }
            usedWords.add(currentWord)
           return shuffleCurrentWord(currentWord)

    }
}