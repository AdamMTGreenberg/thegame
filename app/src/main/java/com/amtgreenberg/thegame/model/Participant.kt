package com.amtgreenberg.thegame.model

import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "Participant", primaryKeys = ["name"])
data class Participant(
    val name: String,
) : Serializable