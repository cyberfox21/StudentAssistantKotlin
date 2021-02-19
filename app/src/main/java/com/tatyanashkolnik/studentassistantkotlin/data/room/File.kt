package com.tatyanashkolnik.studentassistantkotlin.data.room

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_table")
data class File(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val link: String
)
