package com.felixtien.fam.home.dashboard.sub.tenant.graph

sealed class TenantScreen(val route: String) {
    data object Tenant: TenantScreen(route = "tenant_screen")
    data object Second: TenantScreen(route = "second_screen")
}