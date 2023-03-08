package ru.english.data.words

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.english.data.words.model.Word
import ru.english.data.words.model.mapWord

class WordsDataStore {

    private val db = Firebase.database

    fun getWordsList(
        onSuccess: (List<Word>) -> Unit
    ) {
        val words = ArrayList<Word>()
        val startInterval = (1..10).random() * 100
        val endInterval = startInterval + 100

        CoroutineScope(Dispatchers.IO).launch {
            (startInterval..endInterval).forEach {
                getById(it){
                    words.add(it)
                }
            }

            onSuccess(words)
        }
    }

    private fun getById(
        id: Int,
        onSuccess: (Word) -> Unit
    ){
        db.reference.child("words") .child(id.toString()).get()
            .addOnSuccessListener { onSuccess(it.mapWord()) }
    }
}