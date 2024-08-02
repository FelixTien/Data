package com.felixtien.fam.home.dashboard.sub.landlord.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.felixtien.fam.Navigation
import com.felixtien.fam.home.dashboard.sub.landlord.graph.LandlordScreen
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LandlordScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Landlord",
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.displayLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
        ElevatedButton(onClick = {
            navController.navigate(LandlordScreen.LeaseList.route)
        }) {
            Text(text = "Next")
        }


        val scope = rememberCoroutineScope()
        Button(onClick = {
            scope.launch {
                val token = Firebase.messaging.token.await()
                Log.d("TAG", "token = $token")
            }
        }) {
            Text(text = "click")
        }

        Spacer(modifier = Modifier.weight(0.5f))
        Navigation()
    }
}

@Preview(showBackground = true)
@Composable
private fun LandlordScreenPreview() {
    LandlordScreen(navController = rememberNavController())
}