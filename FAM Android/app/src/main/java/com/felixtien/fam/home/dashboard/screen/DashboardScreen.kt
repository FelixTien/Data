package com.felixtien.fam.home.dashboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.dashboard.sub.landlord.graph.LandlordGraph
import com.felixtien.fam.home.dashboard.sub.tenant.graph.TenantGraph
import com.felixtien.fam.home.management.graph.ManagementGraph
import com.felixtien.fam.home.profile.graph.ProfileGraph

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        when (HomeAPI.mode.intValue) {
            0 -> LandlordGraph(navController = HomeAPI.dashboardController)
            1-> TenantGraph(navController = HomeAPI.dashboardController)
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Navigation()
    }
}
@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    DashboardScreen()
}