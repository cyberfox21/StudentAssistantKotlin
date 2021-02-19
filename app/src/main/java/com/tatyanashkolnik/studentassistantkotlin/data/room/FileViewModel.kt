package com.tatyanashkolnik.studentassistantkotlin.data.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tatyanashkolnik.studentassistantkotlin.data.room.File
import com.tatyanashkolnik.studentassistantkotlin.data.room.FileDatabase
import com.tatyanashkolnik.studentassistantkotlin.data.room.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<File>>
    private val repository: FileRepository

    init {
        val fileDao = FileDatabase.getDatabase(application).fileDao()
        repository = FileRepository(fileDao)
        readAllData = repository.readAllData
    }

    fun addFile(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFile(file)
        }
    }
}
