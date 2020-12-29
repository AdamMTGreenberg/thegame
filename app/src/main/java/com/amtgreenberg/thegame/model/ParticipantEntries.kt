package com.amtgreenberg.thegame.model

import androidx.room.Embedded
import androidx.room.Relation

data class ParticipantEntries(
    @Embedded val participant: Participant,
    @Relation(
        parentColumn = "name",
        entityColumn = "participant_name"
    )
    val entries: List<Entry>
)