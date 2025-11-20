package com.example.ecofriendlychallengeapp.viewmodel

import com.example.ecofriendlychallengeapp.data.repository.ChallengeRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry
import com.example.ecofriendlychallengeapp.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

open class ChallengeViewModel(app: Application) : AndroidViewModel(app) {

    // Initializing a repository
    private val repository: ChallengeRepository
    val entries: LiveData<List<ChallengeLogEntry>>

    // Encapsulation _message as mutable
    private val _message = MutableLiveData<String>()
    // Exposing an immutable LiveData to the UI to prevent external state modification
    val message: LiveData<String> = _message

    // Creating LiveData variables observed by the UI:
    val loggedDays = MutableLiveData<Int>()
    val missedDays = MutableLiveData<Int>()
    val passedDays = MutableLiveData<Int>()
    val daysLeft = MutableLiveData<Int>()
    val totalGoalDays = MutableLiveData<Int>()
    val motivationalMessage = MutableLiveData<String>()
    val currentChallenge = MutableLiveData<String>()

    private var goalStartDate: LocalDate = LocalDate.now()
    private var goalDurationDays: Int = 0

    // Creating an observer that observes a list of entries
    private val entryObserver = Observer<List<ChallengeLogEntry>> { list ->
        updateProgressData(list)
    }

    init {
        val dao = AppDatabase.getDatabase(app).challengeDao()
        repository = ChallengeRepository(dao)
        entries = repository.allEntries
        // Ensuring that progress data is always up-to-date
        entries.observeForever(entryObserver)
    }

    // Removing the observer after the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        entries.removeObserver(entryObserver)
    }

    // --- Inserting ---
    // Inserting a new challenge entry in a coroutine
    fun insertEntry(success: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val challenge = currentChallenge.value ?: "Unknown"
        val today = LocalDate.now().toString()

        // Checking if a selected challenge has already been logged by the user for the current day
        val alreadyLoggedToday = repository.checkIfEntryLoggedToday(challenge, today)

        if (!alreadyLoggedToday) {
            val entry = ChallengeLogEntry(
                challenge = challenge,
                date = today,
                success = success
            )
            repository.insertEntry(entry)

            // Notification
            _message.postValue("You successfully logged progress for today!")

        // Notification
        } else {
            _message.postValue("You've already logged progress today! Try again tomorrow.")
        }
    }

    // --- Deleting ---
    // Deleting a challenge entry in a coroutine
    fun deleteEntry() = viewModelScope.launch(Dispatchers.IO) {
        val challenge = currentChallenge.value
        if (challenge.isNullOrBlank()) {
            _message.postValue("No active challenge to delete")
            return@launch
        }

        repository.deleteEntry(challenge)

            // Resetting state
            currentChallenge.postValue("")
            goalDurationDays = 0
            totalGoalDays.postValue(0)
            passedDays.postValue(0)
            loggedDays.postValue(0)
            missedDays.postValue(0)
            daysLeft.postValue(0)

        // Notification
            _message.postValue("Challenge was deleted successfully.")
    }
        // Resetting the _message value after it has been displayed by the UI
        fun clearMessage() {
            _message.value = ""
        }

        // --- Business Logic ---

        fun updateProgressData(entries: List<ChallengeLogEntry>) {
            // Defining today's date
            val today = LocalDate.now()

            // Checking whether a challenge is currently active
            val current = currentChallenge.value ?: return

            // Defining entries belonging only to the current challenge
            val currentEntries = entries.filter { it.challenge == current }

            // Counting how many days have passed since the goal was set
            val passed = (ChronoUnit.DAYS.between(goalStartDate, today) + 1).toInt()
                .coerceIn(0, goalDurationDays)

            // Updating LiveData objects
            passedDays.postValue(passed)
            totalGoalDays.postValue(goalDurationDays)

            // Counting how many logged days were successful
            val logged = currentEntries.count { it.success == true }
            // Counting how many logged days were missed
            val missed = currentEntries.count { it.success == false }

            // Calculating how many days are left to achieve the goal
            val left = (goalDurationDays - passed).coerceAtLeast(0)

            // Updating LiveData values
            passedDays.value = passed
            totalGoalDays.value = goalDurationDays
            loggedDays.value = logged
            missedDays.value = missed
            daysLeft.value = left
        }

        // Enabling the user to set a new goal
        fun setGoal(startDate: LocalDate, durationDays: Int) {
            goalStartDate = startDate
            goalDurationDays = durationDays
            updateProgressData(entries.value ?: emptyList())
            //entries.value?.let { updateProgressData(it) }
        }
}

