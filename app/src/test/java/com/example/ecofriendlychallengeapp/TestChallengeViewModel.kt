package com.example.ecofriendlychallengeapp

import androidx.lifecycle.ViewModel
import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class TestChallengeViewModel(
    private val repository: FakeChallengeRepository
) : ViewModel() {

    private val _passedDays = MutableStateFlow(0)
    val passedDays: StateFlow<Int> = _passedDays.asStateFlow()

    private val _loggedDays = MutableStateFlow(0)
    val loggedDays: StateFlow<Int> = _loggedDays.asStateFlow()

    private val _missedDays = MutableStateFlow(0)
    val missedDays: StateFlow<Int> = _missedDays.asStateFlow()

    private val _daysLeft = MutableStateFlow(0)
    val daysLeft: StateFlow<Int> = _daysLeft.asStateFlow()

    private var durationDays = 0

    fun setGoal(startDate: LocalDate, durationDays: Int) {
        this.durationDays = durationDays
        _passedDays.value = 0
        _loggedDays.value = 0
        _missedDays.value = 0
        _daysLeft.value = durationDays
    }
    fun updateProgressData(entries: List<ChallengeLogEntry>) {
        val passed = entries.count { it.success == true }
        val logged = entries.size
        val missed = (durationDays - logged).coerceAtLeast(0)

        _passedDays.value = passed
        _loggedDays.value = logged
        _missedDays.value = missed
        _daysLeft.value = durationDays - logged
    }
}
