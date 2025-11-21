package com.example.ecofriendlychallengeapp

import androidx.lifecycle.LiveData
import com.example.ecofriendlychallengeapp.data.dao.ChallengeDao
import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeChallengeDao : ChallengeDao {
    private val data = mutableListOf<ChallengeLogEntry>()
    private val _entriesFlow = MutableStateFlow<List<ChallengeLogEntry>>(emptyList())
    val entriesFlow = _entriesFlow.asStateFlow()

    private fun notifyUpdate() {
        _entriesFlow.value = data.sortedByDescending { it.date }
    }

    override suspend fun insertEntry(entry: ChallengeLogEntry) {
        data.removeAll { it.id == entry.id }
        data.add(entry)
        notifyUpdate()
    }

    override suspend fun deleteEntry(challenge: String) {
        data.removeAll { it.challenge == challenge }
        notifyUpdate()
    }

    override suspend fun getEntryBasedOnDate(challenge: String, date: String): ChallengeLogEntry? {
        return data.firstOrNull { it.challenge == challenge && it.date == date }
    }

    override fun getAllEntries(): LiveData<List<ChallengeLogEntry>> {
        throw UnsupportedOperationException("LiveData is not supported.")
    }
}
