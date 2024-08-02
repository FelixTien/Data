package com.felixtien.fam.home.dashboard.sub.landlord.graph

sealed class LandlordScreen(val route: String) {
    data object Landlord: LandlordScreen(route = "landlord_screen")
    data object LeaseList: LandlordScreen(route = "lease_list_screen")
    data object Lease: LandlordScreen(route = "lease_screen")
    data object EditLease: LandlordScreen(route = "edit_lease_screen")
}