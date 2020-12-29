package com.amtgreenberg.thegame.model

import androidx.room.Entity
import java.io.Serializable
import java.time.OffsetDateTime

@Entity(tableName = "entry", primaryKeys = ["entryNumber"])
data class Entry(
    val entryNumber: Int,
    val participant: Participant,
    val date: OffsetDateTime,
    val isValid: Boolean = true,
    val dateWon: OffsetDateTime? = null
) : Serializable
