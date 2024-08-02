package com.felixtien.fam.authentication.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.felixtien.fam.authentication.Google
import com.felixtien.fam.authentication.graph.AuthScreen
import com.felixtien.fam.home.HomeAPI

@Composable
fun AuthScreen(
    navController: NavHostController,
    google: Google
) {
    LaunchedEffect(Unit) {
        AuthAPI.progress = false
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.4f))
        Image(
            painter = painterResource(id = R.drawable.fam),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.weight(0.15f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.15f))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Gray,
                disabledPlaceholderColor = Color.Gray
            ),
            placeholder = { Text(text = "Phone Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .background(Color.White)
                .clickable {
                    navController.navigate(AuthScreen.Phone.route)
                }
        )
        Spacer(modifier = Modifier.weight(0.05f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .padding(horizontal = 10.dp)
                    .background(Color.Gray)
            ){}
            Text(
                text = "or sign in with",
                color = Color.Gray,
                fontSize = 15.sp
            )
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .padding(horizontal = 10.dp)
                    .background(Color.Gray)
            ){}
        }
        Spacer(modifier = Modifier.weight(0.025f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.35f))
            Image(
                painter = painterResource(id = R.drawable.google_btn),
                contentDescription = "Google",
                modifier = Modifier
                    .clickable {
                        AuthAPI.signInGoogle(google = google)
                    }
            )
            Spacer(modifier = Modifier.weight(0.3f))
            Image(
                painter = painterResource(id = R.drawable.email_btn),
                contentDescription = "Google",
                modifier = Modifier
                    .clickable {
                        navController.navigate(AuthScreen.Login.route)
                    }
            )
            Spacer(modifier = Modifier.weight(0.35f))
        }
        Spacer(modifier = Modifier.weight(0.075f))
        Text(
            text = "Account has been deleted.",
            color = if (HomeAPI.problem.value) MaterialTheme.colorScheme.error else Color.Transparent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.15f))
    }
}