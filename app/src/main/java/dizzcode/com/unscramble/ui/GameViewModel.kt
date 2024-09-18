package dizzcode.com.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dizzcode.com.unscramble.data.SCORE_INCREASE
import dizzcode.com.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    /**
     * Note 01
     *
     * StateFlow
     *
     * StateFlow is a data holder observable flow that emits the current and new state updates.
     * Its value property reflects the current state value. To update state and send it to the flow,
     * assign a new value to the value property of the MutableStateFlow class.
     *
     * In Android, StateFlow works well with classes that must maintain an observable immutable state.
     *
     * * A StateFlow can be exposed from the GameUiState so that the composables can listen for
     * UI state updates and make the screen state survive configuration changes.
     */
    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    //The asStateFlow() makes this mutable state flow a read-only state flow.

    /**
     * Note 02
     *
     * Backing property
     *
     * A backing property lets you return something from a getter other than the exact object.
     *
     * For var property, the Kotlin framework generates getters and setters.
     *
     * * For getter and setter methods, you can override one or both of these methods and
     * provide your own custom behavior. To implement a backing property, you override the getter method
     * to return a read-only version of your data.
     */
    // Declare private mutable variable that can only be modified
    // within the class it is declared.
    private var _count = 0

    // Declare another public immutable field and override its getter method.
    // Return the private property's value in the getter method.
    // When count is accessed, the get() function is called and
    // the value of _count is returned.
    val count: Int
        get() = _count

    private lateinit var currentWord: String

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    private fun shuffleCurrentWord(word: String): String{
        val tempWord = word.toCharArray()

        // Scramble the word
        tempWord.shuffle()

        while (String(tempWord) == word){
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    private fun pickRandomWordAndShuffle() : String{
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }


    private fun resetGame(){
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess(){
        if(userGuess.equals(currentWord, ignoreCase = true)){
            // User's guess is correct, increase the score
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }

            /**
             * Note 03
             *
             * Note on copy() method: Use the copy() function to copy an object,
             * allowing you to alter some of its properties while keeping the rest unchanged.
             *
             * Example:
             *
             * val jack = User(name = "Jack", age = 1)
             *
             * val olderJack = jack.copy(age = 2)
             */
        }

        //Reset user guess
        updateUserGuess("")
    }

    private fun updateGameState(updatedScore: Int){
        _uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                score = updatedScore,
                currentWordCount = currentState.currentWordCount.inc()
            )
        }
    }

    fun skipWord(){
        updateGameState(_uiState.value.score)
        //Reset user guess
        updateUserGuess("")
    }
}
