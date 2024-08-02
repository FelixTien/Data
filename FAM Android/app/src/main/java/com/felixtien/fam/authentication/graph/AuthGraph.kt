package com.felixtien.fam.authentication.graph

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.felixtien.fam.authentication.Email
import com.felixtien.fam.authentication.Google
import com.felixtien.fam.authentication.Phone
import com.felixtien.fam.authentication.screen.AuthScreen
import com.felixtien.fam.authentication.screen.CheckScreen
import com.felixtien.fam.authentication.screen.ForgotScreen
import com.felixtien.fam.authentication.screen.LoginScreen
import com.felixtien.fam.authentication.screen.PhoneScreen
import com.felixtien.fam.authentication.screen.VerificationScreen
import com.felixtien.fam.authentication.screen.WelcomeScreen

@Composable
fun AuthGraph(
    navController: NavHostController,
    activity: Activity,
    google: Google
) {
    NavHost(
        navController = navController,
        startDestination = AuthScreen.Auth.route
    ){
        val phone = Phone(activity = activity)
        val email = Email()
        composable(route = AuthScreen.Auth.route){
            AuthScreen(google = google, navController = navController)
        }
        composable(route = AuthScreen.Login.route){
            LoginScreen(navController = navController)
        }
        composable(route = AuthScreen.Forgot.route){
            ForgotScreen(navController = navController, email = email)
        }
        composable(route = AuthScreen.Reset.route){
            CheckScreen(navController = navController, title = "Reset Password Email Sent", content = "Please check your email for the link to reset your password and follow the instructions to create a new one.")
        }
        composable(route = AuthScreen.Welcome.route){
            WelcomeScreen(navController = navController, email = email)
        }
        composable(route = AuthScreen.Create.route){
            CheckScreen(navController = navController, title = "Verification Email Link Sent", content = "Please check your email for the verification link and click on it to verify your account before logging in.")
        }
        composable(route = AuthScreen.Phone.route){
            PhoneScreen(navController = navController, phone = phone)
        }
        composable(route = AuthScreen.Verification.route){
            VerificationScreen(navController = navController)
        }
    }
}