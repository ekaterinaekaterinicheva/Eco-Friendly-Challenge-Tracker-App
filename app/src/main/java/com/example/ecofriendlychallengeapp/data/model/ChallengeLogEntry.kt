package com.example.ecofriendlychallengeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_log_entries")

// This entity defines the challenge_log_entries table
data class ChallengeLogEntry (

    // Assigning a primary key to each field in the entity
    @PrimaryKey(autoGenerate = true)

    // Column names in the table:
    val id: Int = 0,
    val challenge: String?,
    val date: String?,
    val success: Boolean?
    )