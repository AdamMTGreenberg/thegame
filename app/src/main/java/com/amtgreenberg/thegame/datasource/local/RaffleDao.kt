package com.amtgreenberg.thegame.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import com.amtgreenberg.thegame.model.ParticipantEntries

@Dao
interface RaffleDao {

    /**********************************************************
     * QUERIES
     ***********************************************************/

    @Query("SELECT * FROM Participant")
    suspend fun getParticipants(): List<Participant>

    @Query("SELECT * FROM Entry")
    suspend fun getEntries(): List<Entry>

    @Query("SELECT * FROM Entry ORDER BY entry_number DESC LIMIT 1")
    suspend fun getLastEntry(): Int

    @Query("SELECT * FROM Participant WHERE name IS :name")
    suspend fun getParticipantEntries(name: String): List<ParticipantEntries>

    @Query("SELECT * FROM Entry WHERE Entry.participant_name IS :name AND Entry.is_valid = 1")
    suspend fun getActiveParticipantEntries(name: String): List<Entry>

    /**********************************************************
     * INSERTS
     ***********************************************************/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParticipant(vararg participant: Participant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(vararg entry: Entry)

    /**********************************************************
     * UPDATES
     ***********************************************************/

    @Update
    fun updateEntry(vararg entry: Entry)
}