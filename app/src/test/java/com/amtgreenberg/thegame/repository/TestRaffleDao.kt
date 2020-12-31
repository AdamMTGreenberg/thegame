package com.amtgreenberg.thegame.repository

import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.model.Entry
import com.amtgreenberg.thegame.model.Participant
import com.amtgreenberg.thegame.model.ParticipantEntries

class TestRaffleDao : RaffleDao {

    val entries: MutableList<Entry> = mutableListOf()
    val participants: MutableList<Participant> = mutableListOf()

    override suspend fun getParticipants(): List<Participant> = participants

    override suspend fun getEntries(): List<Entry> = entries

    override suspend fun getLastEntry(): Entry? = entries.maxBy { it.entryNumber }

    override suspend fun getValidEntries(): List<Entry> = entries.filter { it.isValid }

    override suspend fun getParticipantEntries(name: String): List<ParticipantEntries> {
        return mutableListOf<ParticipantEntries>().apply {
            participants.forEach { participant ->
                add(
                    ParticipantEntries(
                        participant,
                        entries.filter { participant.name == it.participantName }
                    )
                )
            }
        }
    }

    override suspend fun getActiveParticipantEntries(name: String): List<Entry> {
        return entries.filter { name == it.participantName && it.isValid}
    }

    override fun insertParticipant(vararg participant: Participant) {
        participants.addAll(participant)
    }

    override fun insertEntry(vararg entry: Entry) {
        entries.addAll(entry)
    }

    override fun updateEntry(vararg entry: Entry) {
        entry.forEach { newEntry ->
            val itr = entries.iterator()
            itr.forEachRemaining {
                if (newEntry.entryNumber == it.entryNumber) {
                    itr.remove()
                }
            }
        }
        entries.addAll(entry)
    }
}