package com.felixtien.fam.home.profile.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.Email
import com.felixtien.fam.authentication.EmailException
import com.felixtien.fam.authentication.screen.InputLogin
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.profile.graph.ProfileScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidateScreen(
    navController: NavHostController,
    delete: Boolean,
    email: Email,
    updateEmail: String?,
    updatePassword: String?
) {
    var mail by remember { mutableStateOf(HomeAPI.user.value?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var exception by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description",
                        tint = Color.Black
                    )
                }
            },
            title = {}
        )
        Text(
            text = "Verify Identity",
            color = Color.Black,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        InputLogin(reAuth = true, mail = mail, password = password, exception = exception){ newMail, newPassword ->
            mail = newMail
            password = newPassword
        }
        Spacer(modifier = Modifier.weight(0.2f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.2f))
        ElevatedButton(
            modifier = Modifier.width(150.dp),
            onClick = {
                AuthAPI.progress = true
                if (delete) {
                    AuthAPI.deleteEmail(mail = mail, password = password) { errorMessage ->
                        if (errorMessage == null){
                            navController.navigate(ProfileScreen.SuccessDelete.route)
                        }else {
                            exception = true
                            password = ""
                            error = when (errorMessage) {
                                EmailException.EMPTY -> {
                                    "No empty field."
                                }

                                EmailException.VERIFY -> {
                                    "Account has not yet been verified."
                                }

                                else -> {
                                    "Invalid password."
                                }
                            }
                        }
                    }
                }else{
                    if (password.isEmpty()){
                        exception = true
                        password = ""
                        error = "No empty field."
                        return@ElevatedButton
                    }
                    Firebase.auth.signInWithEmailAndPassword(mail, password)
                        .addOnSuccessListener {
                            updateEmail?.let {
                                email.updateEmail(mail = it) { errorMessage ->
                                    if (errorMessage == null) {
                                        navController.popBackStack()
                                        navController.navigate(ProfileScreen.Sent.route)
                                    }
                                }
                            }
                            updatePassword?.let {
                                email.updatePassword(password = it, confirm = it) { errorMessage ->
                                    if (errorMessage == null){
                                        navController.popBackStack()
                                        navController.navigate(ProfileScreen.SuccessChange.route)
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {
                            exception = true
                            password = ""
                            error = "Invalid password."
                        }
                }
                AuthAPI.progress = false
            }
        ) {
            Text(text = "Log In")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.6f))
    }
}