package ru.english.data.words.model

import com.google.firebase.database.DataSnapshot

data class Word(
    val wordEn: String,
    val wordRu: String
)

fun DataSnapshot.mapWord(): Word {
    return Word(
        wordEn = child("word_en").value.toString(),
        wordRu = child("word_ru").value.toString()
    )
}