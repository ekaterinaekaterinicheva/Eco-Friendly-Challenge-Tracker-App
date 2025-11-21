package com.example.ecofriendlychallengeapp.ui.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicDesign(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    // Defining a layout composable that places its child elements vertically
    Column(
        modifier = modifier
            .fillMaxSize() // Makes the column to fill the whole available space
            .background(MaterialTheme.colorScheme.background)
            .padding(30.dp),
        // Placing the first element in the main axis start and the last item in the main axis end,
        // distributing the rest elements evenly.
        verticalArrangement = Arrangement.SpaceBetween,
        // Placing the elements horizontally in the center
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}
