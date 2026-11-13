package com.example.workflow.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorkflowEntity::class, TaskEntity::class, StepEntity::class, NoteEntity::class], version = 1, exportSchema = false)
abstract class WorkflowDatabase : RoomDatabase() {
    abstract fun workflowDao(): WorkflowDao

    companion object {
        @Volatile
        private var INSTANCE: WorkflowDatabase? = null

        fun getDatabase(context: Context): WorkflowDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorkflowDatabase::class.java,
                    "workflow_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
