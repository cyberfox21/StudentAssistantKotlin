package com.tatyanashkolnik.studentassistantkotlin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [File::class], version = 1, exportSchema = false)
abstract class FileDatabase : RoomDatabase() {

    abstract fun fileDao() : FileDao

    companion object{
        @Volatile
        private var INSTANCE: FileDatabase? = null

        fun getDatabase(context: Context): FileDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileDatabase::class.java,
                    "file_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}