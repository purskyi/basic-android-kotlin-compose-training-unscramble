package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.ui.GameViewModel
import org.jetbrains.annotations.TestOnly
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before

import org.junit.Test


class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdateAndErrorFlagUnset() {

        val correctPlayerWord = viewModel.hacks()
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        var currentGameUiState = viewModel.uiState.value

        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet() {
        val incorrectPlayerWord = "justForTestWrongWord"

        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()

        var currentGameUiState = viewModel.uiState.value

        assertTrue(currentGameUiState.isGuessedWordWrong)
        assertEquals(0, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val GameUiState = viewModel.uiState.value
        val unScrambledWord = viewModel.hacks()
        assertTrue(GameUiState.wordCount == 0)
        assertTrue(GameUiState.score == 0)
        assertFalse(GameUiState.isGuessedWordWrong)
        assertFalse(GameUiState.isGameOver)
    }
    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = viewModel.hacks()

        repeat(MAX_NO_OF_WORDS){
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = viewModel.hacks()
            assertEquals(expectedScore,currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.wordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

@Test
fun gameViewModel_SkipWorksCorrectly(){
    var currentGameUiState = viewModel.uiState.value

    repeat(MAX_NO_OF_WORDS){
        viewModel.skip()
        currentGameUiState = viewModel.uiState.value
        assertEquals(0,currentGameUiState.score)
    }
    assertEquals(MAX_NO_OF_WORDS, currentGameUiState.wordCount)
    assertTrue(currentGameUiState.isGameOver)

}


    companion object{
        private  const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}