package com.felixtien.fam.authentication.graph

sealed class AuthScreen(val route: String) {
    data object Auth: AuthScreen(route = "auth_screen")
    data object Login: AuthScreen(route = "login_screen")
    data object Forgot: AuthScreen(route = "forgot_screen")
    data object Reset: AuthScreen(route = "reset_screen")
    data object Welcome: AuthScreen(route = "welcome_screen")
    data object Create: AuthScreen(route = "create_screen")
    data object Phone: AuthScreen(route = "phone_screen")
    data object Verification: AuthScreen(route = "verification_screen")
}