package com.tatyanashkolnik.studentassistantkotlin.data.room

import androidx.lifecycle.LiveData
import com.tatyanashkolnik.studentassistantkotlin.data.room.File
import com.tatyanashkolnik.studentassistantkotlin.data.room.FileDao

class FileRepository(private val fileDao: FileDao) {

    val readAllData: LiveData<List<File>> = fileDao.readAllData()

    suspend fun addFile(file: File){
        fileDao.addFile(file)
    }
}
