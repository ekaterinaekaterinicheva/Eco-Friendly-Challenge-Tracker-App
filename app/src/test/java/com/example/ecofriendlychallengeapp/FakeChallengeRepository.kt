package com.example.ecofriendlychallengeapp

import androidx.lifecycle.LiveData
import com.example.ecofriendlychallengeapp.data.dao.ChallengeDao
import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry

class FakeChallengeRepository(private val dao: ChallengeDao) {

    val allEntries: LiveData<List<ChallengeLogEntry>> = dao.getAllEntries()

    suspend fun insertEntry(entry: ChallengeLogEntry) = dao.insertEntry(entry)

    suspend fun deleteEntry(challenge: String) = dao.deleteEntry(challenge)

    suspend fun getEntryBasedOnDate(challenge: String, date: String) =
        dao.getEntryBasedOnDate(challenge, date)
}
