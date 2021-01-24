package com.tatyanashkolnik.studentassistantkotlin.data

import androidx.lifecycle.LiveData

class FileRepository(private val fileDao: FileDao) {

    val readAllData: LiveData<List<File>> = fileDao.readAllData()
}
