package com.felixtien.fam.home.profile.graph

sealed class ProfileScreen(val route: String) {
    data object Profile: ProfileScreen(route = "profile_screen")
    data object Edit: ProfileScreen(route = "edit_screen")
    data object Code: ProfileScreen(route = "code_screen")
    data object Select: ProfileScreen(route = "select_screen")
    data object EmailDelete: ProfileScreen(route = "email_delete_screen")
    data object EmailChange: ProfileScreen(route = "email_change_screen")
    data object UpdatePassword: ProfileScreen(route = "update_password_screen")
    data object UpdateEmail: ProfileScreen(route = "update_email_screen")
    data object Sent: ProfileScreen(route = "sent_screen")
    data object SuccessDelete: ProfileScreen(route = "success_delete_screen")
    data object SuccessChange: ProfileScreen(route = "success_change_screen")
}