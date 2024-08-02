package com.felixtien.fam.home.profile.screen

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.felixtien.fam.home.profile.graph.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectScreen(
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
        LaunchedEffect(Unit) {
            google.errorMatch = ""
        }
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
        Spacer(modifier = Modifier.weight(0.3f))
        Image(
            painter = painterResource(id = R.drawable.fam),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
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
                text = "verify identity",
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
        Spacer(modifier = Modifier.weight(0.1f))
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
                        AuthAPI.deleteGoogle(google = google)
                    }
            )
            Spacer(modifier = Modifier.weight(0.3f))
            Image(
                painter = painterResource(id = R.drawable.email_btn),
                contentDescription = "Google",
                modifier = Modifier
                    .clickable {
                        navController.navigate(ProfileScreen.EmailDelete.route)
                    }
            )
            Spacer(modifier = Modifier.weight(0.35f))
        }
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = google.errorMatch,
            color = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-15).dp)
        )
        Spacer(modifier = Modifier.weight(0.2f))
    }
}