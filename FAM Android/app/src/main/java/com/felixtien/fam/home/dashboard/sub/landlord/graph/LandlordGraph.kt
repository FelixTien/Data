package com.felixtien.fam.home.dashboard.sub.landlord.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.felixtien.fam.home.dashboard.sub.landlord.screen.EditLeaseScreen
import com.felixtien.fam.home.dashboard.sub.landlord.screen.LandlordScreen
import com.felixtien.fam.home.dashboard.sub.landlord.screen.LeaseListScreen
import com.felixtien.fam.home.dashboard.sub.landlord.screen.LeaseScreen

@Composable
fun LandlordGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = LandlordScreen.Landlord.route
    ){
        composable(route = LandlordScreen.Landlord.route){
            LandlordScreen(navController = navController)
        }
        composable(route = LandlordScreen.LeaseList.route){
            LeaseListScreen(navController = navController)
        }
        composable(route = LandlordScreen.Lease.route + "/{index}"){ navBackStack ->
            val index = navBackStack.arguments?.getString("index")?.toInt() ?: 0
            LeaseScreen(navController = navController, index = index)
        }
        composable(route = LandlordScreen.EditLease.route + "/{index}"){ navBackStack ->
            val index = navBackStack.arguments?.getString("index")?.toInt() ?: 0
            EditLeaseScreen(navController = navController, index = index)
        }
    }
}