package ru.english.data.words

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.english.data.words.model.Word
import ru.english.data.words.model.WordsLevel
import ru.english.data.words.model.mapWord

class WordsDataStore {

    private val db = Firebase.database

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
        val number = (0..1000).random()

        db.reference.child("words").child(number.toString()).get()
            .addOnSuccessListener {
                onSuccess(it.mapWord())
            }
    }
}