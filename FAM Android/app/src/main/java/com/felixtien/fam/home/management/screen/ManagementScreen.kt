package com.felixtien.fam.home.management.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.felixtien.fam.Navigation
import com.felixtien.fam.home.management.graph.ManagementScreen

@Composable
fun ManagementScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Management",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        ElevatedButton(onClick = {
            navController.navigate(ManagementScreen.Click.route)
        }) {
            Text(text = "Next")
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Navigation()
    }
}

@Preview(showBackground = true)
@Composable
private fun ManagementScreenPreview() {
    ManagementScreen(navController = rememberNavController())
}