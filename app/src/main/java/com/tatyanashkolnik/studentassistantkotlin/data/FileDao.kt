package com.tatyanashkolnik.studentassistantkotlin.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFile(file: File)

    @Query("SELECT * FROM file_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<File>>
}
