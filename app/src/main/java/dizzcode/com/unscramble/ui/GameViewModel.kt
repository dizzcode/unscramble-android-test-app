package dizzcode.com.unscramble.ui

import androidx.lifecycle.ViewModel
import dizzcode.com.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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


    fun resetGame(){
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }
}
