package com.amtgreenberg.thegame.repository

import com.amtgreenberg.thegame.data.ResultData
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.di.IoDispatcher
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.time.Month
import java.time.OffsetDateTime

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
        return try {
            val nextEntry = raffleDao.getLastEntry() + 1

            // If no entries exist, add the participant
            if (raffleDao.getParticipantEntries(participant.name).isEmpty()) {
                addParticipant(participant)
            }

            val entry = Entry(nextEntry, participant.name, OffsetDateTime.now())
            withContext(ioDispatcher) { raffleDao.insertEntry(entry) }
            ResultData.Success(entry)
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun addParticipant(participant: Participant): ResultData<Participant> {
        return try {
            withContext(ioDispatcher) { raffleDao.insertParticipant(participant) }
            ResultData.Success(participant)
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun addParticipantEntries(
        participant: Participant,
        entryIds: List<Int>,
        offsetMonthEntries: Int
    ): ResultData<List<Entry>> {
        return try {
            // If no entries exist, add the participant
            if (raffleDao.getParticipantEntries(participant.name).isEmpty()) {
                addParticipant(participant)
            }

            val sortedEntries = sortDesc(entryIds)
            val beginningMonth = getBeginningMonth(offsetMonthEntries)

            val entries = sortedEntries.mapIndexed { index, entryId ->
                val entry = Entry(
                    entryNumber = entryId,
                    participantName = participant.name,
                    date = OffsetDateTime.from(beginningMonth.minus(index.toLong()))
                )
                withContext(ioDispatcher) { raffleDao.insertEntry(entry) }
                entry
            }

            return ResultData.Success(entries)
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    override suspend fun obtainRaffleWinner(shouldFinalizeAfterSelection: Boolean): ResultData<Entry> {
        TODO("Not yet implemented")
    }

    /**
     * Ensure that the list of entry IDs is sorted correctly
     */
    private fun sortDesc(entryIds: List<Int>): List<Int> = entryIds.sortedDescending()

    /**
     * We assume that the latest entry ID comes from the previous month, therefore we offset the
     * entry dates with the last month. This is necessary since if someone misses a month, their
     * historic entries are still valid, but they won't be included in the current raffle. This
     * allows us to gate by simple [Entry] is in current month check, and then pull all of those
     * [Participant]'s entries for the raffle.
     */
    private fun getBeginningMonth(offsetMonthEntries: Int): Month {
        val currentMonth = OffsetDateTime.now().month
        return currentMonth.minus(offsetMonthEntries.toLong())
    }
}