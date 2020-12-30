package com.amtgreenberg.thegame.repository

import com.amtgreenberg.thegame.data.ResultData
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant

/**
 * Repository which manages the raffle entries
 */
interface RaffleRepository {

    suspend fun addEntry(participant: Participant): ResultData<Entry>
    suspend fun addParticipant(participant: Participant): ResultData<Participant>
    suspend fun addParticipantEntries(
        participant: Participant,
        entryIds: List<Int>
    ): ResultData<List<Entry>>

    suspend fun obtainRaffleWinner(shouldFinalizeAfterSelection: Boolean = true): ResultData<Entry>
}