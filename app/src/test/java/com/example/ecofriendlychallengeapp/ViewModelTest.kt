package com.example.ecofriendlychallengeapp

import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate

class BusinessLogicUnitTest {

    @Test
    fun calculatesCorrectValues() = runTest {
        @Test
        fun calculatesCorrectValues() = runTest {

            val dao = FakeChallengeDao()
            val repo = FakeChallengeRepository(dao)
            val viewModel = TestChallengeViewModel(repo)

            val today = LocalDate.now()
            viewModel.setGoal(today.minusDays(2), 7)

            val entries = listOf(
                ChallengeLogEntry(
                    id = 0,
                    challenge = "Challenge option 1",
                    date = today.minusDays(2).toString(),
                    success = true
                ),
                ChallengeLogEntry(
                    id = 1,
                    challenge = "Challenge option 1",
                    date = today.minusDays(1).toString(),
                    success = false
                )
            )

            viewModel.updateProgressData(entries)

            assertEquals(1, viewModel.passedDays.value)
            assertEquals(2, viewModel.loggedDays.value)
            assertEquals(0, viewModel.missedDays.value)
            assertEquals(5, viewModel.daysLeft.value)
        }
    }
}
