package com.felixtien.fam.home.dashboard.sub.landlord.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.felixtien.fam.Navigation
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.AuthProvider
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.dashboard.sub.landlord.graph.LandlordScreen
import com.felixtien.fam.home.profile.graph.ProfileScreen
import com.felixtien.fam.home.profile.screen.Mode
import com.felixtien.fam.home.profile.screen.Option
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaseScreen(
    navController: NavHostController,
    index: Int
) {
    val cloud = Firebase.storage.reference

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            cloud.child(HomeAPI.leases[index].lease?.path ?: "").downloadUrl.addOnSuccessListener {
                HomeAPI.leases[index].lease?.url?.value = it.toString()
            }
        }
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            title = {},
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                Row {
                    IconButton(onClick = {
                        
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.person_add),
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(LandlordScreen.EditLease.route + "/$index")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Localized description"
                        )
                    }
                }
            }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .size(210.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {}
                    Box(
                        modifier = Modifier.padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val url = HomeAPI.leases[index].lease?.url?.value.orEmpty()
                        if (url.isEmpty()) {
                            Image(
                                painter = painterResource(id = R.drawable.lease),
                                contentDescription = "Lease",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                                modifier = Modifier.size(200.dp)
                            )
                        }else{
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.onError)
                            Image(
                                painter = rememberAsyncImagePainter(HomeAPI.leases[index].lease?.url?.value),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(15.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                Text(
                    text = HomeAPI.leases[index].lease?.title ?: "",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${HomeAPI.leases[index].lease?.type}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                )
            }
        }
        Navigation()
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaseScreenPreview() {
    LeaseScreen(navController = rememberNavController(), index = 20)
}