package com.amtgreenberg.thegame.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable
import java.time.OffsetDateTime

@Entity(
    tableName = "entry",
    primaryKeys = ["entry_number"],
    indices = [Index(value = ["entry_number", "is_valid"])]
)
data class Entry(
    @ColumnInfo(name = "entry_number") val entryNumber: Int,
    @ColumnInfo(name = "participant_name") val participantName: String,
    val date: OffsetDateTime,
    @ColumnInfo(name = "is_valid") val isValid: Boolean = true,
    @ColumnInfo(name = "date_won") val dateWon: OffsetDateTime? = null
) : Serializable
