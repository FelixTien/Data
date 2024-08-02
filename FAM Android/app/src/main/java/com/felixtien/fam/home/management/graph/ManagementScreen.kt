package com.felixtien.fam.home.management.graph

sealed class ManagementScreen(val route: String) {
    data object Management: ManagementScreen(route = "management_screen")
    data object Click: ManagementScreen(route = "click_screen")
}