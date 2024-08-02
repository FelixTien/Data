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
import com.felixtien.fam.authentication.screen.InputForgot
import com.felixtien.fam.home.profile.graph.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEmailScreen(
    navController: NavHostController,
    email: Email
)  {
    var mail by remember { mutableStateOf("") }
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
        Image(
            painter = painterResource(id = R.drawable.update),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        InputForgot(mail = mail, exception = exception) { mail = it }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = "Enter your desired email address and click the button. You'll receive a verification link to confirm the change.",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        )
        Spacer(modifier = Modifier.weight(0.25f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.25f))
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
        Spacer(modifier = Modifier.height(15.dp))
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 50.dp),
            onClick = {
                AuthAPI.progress = true
                email.updateEmail(mail = mail) { errorMessage ->
                    if (errorMessage == null){
                        navController.popBackStack()
                        navController.navigate(ProfileScreen.Sent.route)
                    }else{
                        when (errorMessage) {
                            EmailException.EMPTY -> {
                                error = "No empty field."
                            }
                            EmailException.INVALID -> {
                                error = "Invalid email."
                            }
                            EmailException.EXIST -> {
                                error ="The email has been registered."
                            }
                            EmailException.AUTH -> {
                                navController.popBackStack()
                                navController.navigate(ProfileScreen.EmailChange.route + "/${mail}/${null}")
                            }
                            else -> {
                                error = "Sever error."
                            }
                        }
                        mail = ""
                        exception = true
                    }
                }
                AuthAPI.progress = false
            }
        ) {
            Text(
                text = "Send Verification Link",
                fontSize = 20.sp
            )
        }
    }
}