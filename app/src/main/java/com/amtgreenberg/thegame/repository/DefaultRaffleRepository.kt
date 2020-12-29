package com.amtgreenberg.thegame.repository

import com.amtgreenberg.thegame.data.ResultData
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.di.IoDispatcher
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Default implementation of the repository: [RaffleRepository]. This handles all the logical
 * organization of the data. Since it is not connected to any remote data, we can simply focus on
 * the local data store.
 */
class DefaultRaffleRepository(
    private val raffleDao: RaffleDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RaffleRepository {

    override suspend fun addEntry(participant: Participant): ResultData<Entry> {
        TODO("Not yet implemented")
    }

    override suspend fun addParticipant(name: Participant): ResultData<Entry> {
        TODO("Not yet implemented")
    }

    override suspend fun addParticipantEntries(
        participant: Participant,
        entryIds: List<Int>
    ): ResultData<List<Entry>> {
        TODO("Not yet implemented")
    }

    override suspend fun obtainRaffleWinner(shouldFinalizeAfterSelection: Boolean): ResultData<Entry> {
        TODO("Not yet implemented")
    }
}