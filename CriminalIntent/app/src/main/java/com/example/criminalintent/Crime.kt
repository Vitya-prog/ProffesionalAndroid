package com.example.criminalintent

import android.text.format.DateFormat
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Crime(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String="Example",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var suspect:String = ""

 ){
    val photoFileName
    get() = "IMG_$id.jpg"
}