package com.example.ecofriendlychallengeapp.ui.history

import com.example.ecofriendlychallengeapp.viewmodel.ChallengeViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecofriendlychallengeapp.data.model.ChallengeLogEntry
import com.example.ecofriendlychallengeapp.ui.reusable.BasicDesign

    @Composable
    fun HistoryScreen(navController: NavController, viewModel: ChallengeViewModel) {
        val entries by viewModel.entries.observeAsState(emptyList())

        BasicDesign {
            // --- History ---
            Text("History", style = MaterialTheme.typography.headlineSmall)
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(25.dp))

            // Composing and laying out the currently visible items in a vertically scrolling list via LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(entries) { entry ->
                    HistoryRow(entry)
                }
            }
        }
    }

    @Composable
    fun HistoryRow(entry: ChallengeLogEntry) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Positioning date of the logged challenge entry at the top (on the left)
            entry.date?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Challenge name (left) and status (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                entry.challenge?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Text(
                    text = if (entry.success == true) "Success" else "Missed",
                    color = if (entry.success == true) Color(0xFF388E3C) else Color(0xFFD32F2F),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
