package ru.english.data.words.model

import com.google.firebase.database.DataSnapshot

enum class WordsLevel(val text:String, val startId:Int, val endId: Int) {
    FIRST("Первый\nуровень", 1,200),
    SECOND("Второй\nуровень",200,400),
    THIRD("Третий\nуровень",400,600),
    FOURTH("Четвертый\nуровень",600,800),
    FIFTH("Пятый\nуровень",800,1000)
}

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