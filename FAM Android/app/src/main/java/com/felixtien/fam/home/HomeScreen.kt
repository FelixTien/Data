package com.felixtien.fam.home

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.felixtien.fam.authentication.Google
import com.felixtien.fam.home.dashboard.screen.DashboardScreen
import com.felixtien.fam.home.management.graph.ManagementGraph
import com.felixtien.fam.home.profile.graph.ProfileGraph

@Composable
fun HomeScreen(
    activity: Activity,
    google: Google
) {
    HomeAPI.dashboardController = rememberNavController()
    HomeAPI.managementController = rememberNavController()
    HomeAPI.profileController = rememberNavController()
    when (HomeAPI.page.intValue) {
        0 -> DashboardScreen()
        1 -> ManagementGraph(navController = HomeAPI.managementController)
        2 -> ProfileGraph(navController = HomeAPI.profileController, activity = activity, google = google)
    }
}