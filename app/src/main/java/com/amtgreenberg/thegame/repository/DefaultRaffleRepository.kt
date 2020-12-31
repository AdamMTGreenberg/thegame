package com.amtgreenberg.thegame.repository

import androidx.annotation.VisibleForTesting
import com.amtgreenberg.thegame.data.ResultData
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.di.IoDispatcher
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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
            val nextEntry = raffleDao.getLastEntry().entryNumber + 1

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
                    date = OffsetDateTime.from(beginningMonth.minus(index.toLong())) // TODO test that subtracting a month at the end of March wouldn't skip February
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
        return try {
            val now = OffsetDateTime.now()
            val lastMonth = getLastMonth(now)
            val thisMonthsEntries = getThisMonthsEntries(lastMonth)

            // Build the set of entries
            val raffleEntries = getAllOfThisMonthsEntries(thisMonthsEntries)

            // Select a winner
            val winner = getWinner(raffleEntries)

            // If we want to finalize this, set all the winner's previous entries to invalid
            if (shouldFinalizeAfterSelection) {
                invalidateOldEntries(raffleEntries, winner, now)
            }

            ResultData.Success(winner)
        } catch (e: Exception) {
            ResultData.Error(e)
        }
    }

    /**
     * Updates the raffle [Entry]s for the winner. Marks the current winning [Entry] as the winner &
     * then marks all other historic & current [Entry]s for the [Participant] who won as invalid.
     */
    @VisibleForTesting
    internal suspend fun invalidateOldEntries(
        raffleEntries: List<Entry>,
        winner: Entry,
        now: OffsetDateTime
    ) {
        withContext(ioDispatcher) {
            raffleDao.updateEntry(
                Entry(
                    winner.entryNumber,
                    winner.participantName,
                    winner.date,
                    false,
                    dateWon = now
                )
            )
            raffleEntries
                .stream()
                .filter {
                    it.participantName == winner.participantName && it.entryNumber != winner.entryNumber
                }
                .forEach {
                    raffleDao.updateEntry(
                        Entry(
                            it.entryNumber,
                            it.participantName,
                            it.date,
                            false
                        )
                    )
                }
        }
    }

    /**
     * Obtains a the last month
     */
    @VisibleForTesting
    internal fun getLastMonth(now: OffsetDateTime) = now.month.minus(1L)

    /**
     * Obtains a list of the current month's entries
     */
    @VisibleForTesting
    internal suspend fun getThisMonthsEntries(lastMonth: Month): List<Entry> =
        raffleDao.getValidEntries().filter {
            it.date.isAfter(OffsetDateTime.from(lastMonth))
        }

    /**
     * Obtains a list of the current month's entries including historical entries
     */
    @VisibleForTesting
    internal suspend fun getAllOfThisMonthsEntries(thisMonthsEntries: List<Entry>): List<Entry> =
        mutableListOf<Entry>().apply {
            thisMonthsEntries.forEach {
                addAll(
                    raffleDao.getActiveParticipantEntries(it.participantName)
                )
            }
        }

    /**
     * Simple method to obtain a winner
     */
    private fun getWinner(raffleEntries: List<Entry>): Entry = raffleEntries.random()

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
    @VisibleForTesting
    internal fun getBeginningMonth(offsetMonthEntries: Int): Month {
        val currentMonth = OffsetDateTime.now().month
        return currentMonth.minus(offsetMonthEntries.toLong())
    }
}