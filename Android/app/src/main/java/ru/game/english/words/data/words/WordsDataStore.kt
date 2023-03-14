package ru.game.english.words.data.words

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.game.english.words.data.utils.UtilsDataStore
import ru.game.english.words.data.words.model.Word
import ru.game.english.words.data.words.model.WordsLevel
import ru.game.english.words.data.words.model.mapWord
import kotlin.collections.ArrayList

class WordsDataStore {

    private val db = Firebase.database
    private val utilsDataStore = UtilsDataStore()
    private var wordsCount = 0

    init {
        utilsDataStore.getWordsCount { wordsCount = it }
    }

    fun getWordsList(
        wordsLevel: WordsLevel = WordsLevel.FIRST,
        onSuccess: (List<Word>) -> Unit
    ) {
        val words = ArrayList<Word>()

        db.reference.child("words")
            .orderByKey()
            .startAfter(wordsLevel.startId.toString())
            .endBefore(wordsLevel.endId.toString())
            .get()
            .addOnSuccessListener {
                it.children.forEach {
                    words.add(it.mapWord())
                }
                onSuccess(words)
            }
    }

    fun getRandomWord(
        onSuccess: (Word) -> Unit
    ) {
        val number = (0..wordsCount).random()

        db.reference.child("words").child(number.toString()).get()
            .addOnSuccessListener {
                onSuccess(it.mapWord())
            }
    }

    fun add(word:Word, onSuccess: () -> Unit){

        db.reference.child("words").child((wordsCount + 1).toString()).setValue(word)
            .addOnSuccessListener {
                utilsDataStore.updateWordsCount(wordsCount + 1){
                    onSuccess()
                }
            }
    }
}