package com.amtgreenberg.thegame.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "entry", primaryKeys = ["name"])
data class Participant(
    val name: String,
    val entries: List<Int>,
) : Serializable