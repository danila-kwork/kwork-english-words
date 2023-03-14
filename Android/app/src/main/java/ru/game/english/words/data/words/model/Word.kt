package ru.game.english.words.data.words.model

import com.google.firebase.database.DataSnapshot

enum class WordsLevel(val text:String, val startId:Int, var endId: Int) {
    FIRST("Первый уровень", 1,200),
    SECOND("Второй уровень",200,400),
    THIRD("Третий уровень",400,600),
    FOURTH("Четвертый уровень",600,800),
    FIFTH("Пятый уровень",800,1000),
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