package com.amtgreenberg.thegame.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.OffsetDateTimeConverters
import com.amtgreenberg.thegame.model.Participant

@Database(entities = [Participant::class, Entry::class], version = 1)
@TypeConverters(OffsetDateTimeConverters::class)
abstract class RaffleDb : RoomDatabase() {

    abstract fun raffleDao(): RaffleDao

    companion object {
        private const val DB_NAME = "raffle_db"
        @Volatile private var INSTANCE: RaffleDb? = null

        fun getDatabase(context: Context): RaffleDb {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, RaffleDb::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}