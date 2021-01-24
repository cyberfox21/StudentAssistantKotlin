package com.tatyanashkolnik.studentassistantkotlin.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FileViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData: LiveData<List<File>>
    private val repository: FileRepository

    init {
        val fileDao = FileDatabase.getDatabase(application).fileDao()
        repository = FileRepository(fileDao)
        readAllData = repository.readAllData
    }

    fun addFile() {}
}
