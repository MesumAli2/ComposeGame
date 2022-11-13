package com.example.android.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    val currentScrambledWord : String = ""
    //Game Ui state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord: String
    var userWords :MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set


    private fun pickRandomWordAndShuffle(): String{
        currentWord = allWords.random()
        if (userWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }else{
            userWords.add(currentWord)
            return  shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWords = word.toCharArray()
        //Scramble the word
        tempWords.shuffle()
        while (String(tempWords).equals(word)){
            tempWords.shuffle()
        }
        return String(tempWords)
    }

    fun checkUserCheck(){
        if (userGuess.equals(currentWord, ignoreCase = true)){

        }else{
            _uiState.update{
                currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
    }
    fun updateUserGuess(guessWord: String){
        userGuess = guessWord
    }


    fun resetGame(){
        userWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }



    init {
        resetGame()
    }
}