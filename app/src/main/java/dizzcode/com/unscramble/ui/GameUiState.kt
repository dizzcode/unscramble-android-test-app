package dizzcode.com.unscramble.ui

data class GameUiState (
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false
)
