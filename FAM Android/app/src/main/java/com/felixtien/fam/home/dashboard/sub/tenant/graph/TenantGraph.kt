package com.felixtien.fam.home.dashboard.sub.tenant.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.felixtien.fam.home.dashboard.sub.tenant.screen.SecondScreen
import com.felixtien.fam.home.dashboard.sub.tenant.screen.TenantScreen

@Composable
fun TenantGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TenantScreen.Tenant.route
    ){
        composable(route = TenantScreen.Tenant.route){
            TenantScreen(navController = navController)
        }
        composable(route = TenantScreen.Second.route){
            SecondScreen(navController = navController)
        }
    }
}