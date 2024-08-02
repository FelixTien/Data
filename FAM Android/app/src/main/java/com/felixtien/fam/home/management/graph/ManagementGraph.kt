package com.felixtien.fam.home.management.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.felixtien.fam.home.management.screen.ClickScreen
import com.felixtien.fam.home.management.screen.ManagementScreen

@Composable
fun ManagementGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ManagementScreen.Management.route
    ){
        composable(route = ManagementScreen.Management.route){
            ManagementScreen(navController = navController)
        }
        composable(route = ManagementScreen.Click.route){
            ClickScreen(navController = navController)
        }
    }
}