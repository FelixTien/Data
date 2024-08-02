package com.felixtien.fam.home.profile.graph

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.felixtien.fam.BackPic
import com.felixtien.fam.authentication.Email
import com.felixtien.fam.authentication.Google
import com.felixtien.fam.authentication.Phone
import com.felixtien.fam.home.profile.screen.CodeScreen
import com.felixtien.fam.home.profile.screen.EditScreen
import com.felixtien.fam.home.profile.screen.ProfileScreen
import com.felixtien.fam.home.profile.screen.SelectScreen
import com.felixtien.fam.home.profile.screen.SentScreen
import com.felixtien.fam.home.profile.screen.SuccessScreen
import com.felixtien.fam.home.profile.screen.UpdateEmailScreen
import com.felixtien.fam.home.profile.screen.UpdatePasswordScreen
import com.felixtien.fam.home.profile.screen.ValidateScreen

@Composable
fun ProfileGraph(
    navController: NavHostController,
    activity: Activity,
    google: Google
) {
    NavHost(
        navController = navController,
        startDestination = ProfileScreen.Profile.route
    ){
        val phone = Phone(activity = activity)
        val email = Email()
        composable(route = ProfileScreen.Profile.route){
            ProfileScreen(navController = navController, phone = phone)
        }
        composable(route = ProfileScreen.Edit.route){
            EditScreen(navController = navController)
        }
        composable(route = ProfileScreen.Code.route){
            BackPic(outside = false)
            CodeScreen(navController = navController)
        }
        composable(route = ProfileScreen.Select.route){
            BackPic(outside = false)
            SelectScreen(navController = navController, google = google)
        }
        composable(route = ProfileScreen.EmailDelete.route){
            BackPic(outside = false)
            ValidateScreen(navController = navController, delete = true, email = email, updateEmail = null, updatePassword = null)
        }
        composable(route = (ProfileScreen.EmailChange.route + "/{updateEmail}/{updatePassword}")){ navBackStack ->
            val updateEmail = navBackStack.arguments?.getString("updateEmail")
            val updatePassword = navBackStack.arguments?.getString("updatePassword")
            BackPic(outside = false)
            ValidateScreen(navController = navController, delete = false, email = email, updateEmail = updateEmail, updatePassword = updatePassword)
        }
        composable(route = ProfileScreen.UpdatePassword.route){
            BackPic(outside = false)
            UpdatePasswordScreen(navController = navController, email = email)
        }
        composable(route = ProfileScreen.UpdateEmail.route){
            BackPic(outside = false)
            UpdateEmailScreen(navController = navController, email = email)
        }
        composable(route = ProfileScreen.Sent.route){
            BackPic(outside = false)
            SentScreen(navController = navController)
        }
        composable(route = ProfileScreen.SuccessDelete.route){
            BackPic(outside = false)
            SuccessScreen(navController = navController, title = "Your Account Has Been Deleted", delete = true)
        }
        composable(route = ProfileScreen.SuccessChange.route){
            BackPic(outside = false)
            SuccessScreen(navController = navController, title = "Your Password Has Been Changed", delete = false)
        }
    }
}