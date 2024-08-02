package com.felixtien.fam.home.profile.screen

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.felixtien.fam.Navigation
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.AuthProvider
import com.felixtien.fam.authentication.Phone
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.profile.graph.ProfileScreen
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    phone: Phone
) {
    var url by remember { mutableStateOf(HomeAPI.user.value?.photoURL) }
    val cloud = Firebase.storage.reference
    var showSignOut by remember { mutableStateOf(false) }
    var showDelete by remember { mutableStateOf(false) }
    var emailProvider by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            emailProvider = AuthAPI.providersData().contains(AuthProvider.EMAIL)
            cloud.child(HomeAPI.user.value?.path ?: "").downloadUrl.addOnSuccessListener {
                url = it.toString()
            }
        }
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            title = {},
            actions = {
                IconButton(onClick = {
                    navController.navigate(ProfileScreen.Edit.route)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Localized description"
                    )
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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onError)
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(15.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    text = HomeAPI.user.value?.username ?: "No name",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID : ${HomeAPI.user.value?.uid ?: "Not set"}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                )
                HomeAPI.user.value?.phone?.let {
                    Option(
                        info = true,
                        image = R.drawable.cellphone,
                        text = it,
                        fontSize = 16,
                        warning = false,
                        click = {}
                    )
                }
                HomeAPI.user.value?.email?.let {
                    Option(
                        info = true,
                        image = R.drawable.email,
                        text = it,
                        fontSize = 16,
                        warning = false,
                        click = {}
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "Language",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(0.2f))
                    Image(
                        painter = painterResource(id = R.drawable.english),
                        contentDescription = "English",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Image(
                        painter = painterResource(id = R.drawable.japanese),
                        contentDescription = "English",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Image(
                        painter = painterResource(id = R.drawable.taiwanese),
                        contentDescription = "English",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                    Image(
                        painter = painterResource(id = R.drawable.korean),
                        contentDescription = "English",
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clickable {

                            }
                    )
                    Spacer(modifier = Modifier.weight(0.2f))
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "Mode",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Mode()
                Spacer(modifier = Modifier.padding(10.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "Profile Options",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                if (emailProvider) {
                    Option(
                        info = false,
                        image = R.drawable.password,
                        text = "Change Password",
                        fontSize = 20,
                        warning = false,
                        click = {
                            navController.navigate(ProfileScreen.UpdatePassword.route)
                        }
                    )
                    Option(
                        info = false,
                        image = R.drawable.emailchange,
                        text = "Change Email",
                        fontSize = 20,
                        warning = false,
                        click = {
                            navController.navigate(ProfileScreen.UpdateEmail.route)
                        }
                    )
                }
                Option(
                    info = false,
                    image = R.drawable.signout,
                    text = "Sign Out",
                    fontSize = 20,
                    warning = false,
                    click = {
                        showSignOut = true
                    }
                )
                Option(
                    info = false,
                    image = R.drawable.delete,
                    text = "Delete Account",
                    fontSize = 20,
                    warning = true,
                    click = {
                        showDelete = true
                    }
                )
                Spacer(modifier = Modifier.weight(0.5f))
            }
        }
        Navigation()

        // Dialogs
        if (showSignOut) {
            AlertDialog(
                onDismissRequest = { showSignOut = false },
                title = {
                    Text(
                        text = "Sign Out",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "You have successfully signed out.",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            AuthAPI.signOut()
                            HomeAPI.problem.value = false
                            HomeAPI.page.intValue = 0
                        }
                    ) {
                        Text(
                            text = "OK",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                properties = DialogProperties(dismissOnClickOutside = false)
            )
        }
        if (showDelete) {
            AlertDialog(
                onDismissRequest = { showDelete = false },
                title = {
                    Text(
                        text = "Warning",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete your account?",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            AuthAPI.deleteAccount()
                                .addOnSuccessListener {
                                    navController.navigate(ProfileScreen.SuccessDelete.route)
                                }
                                .addOnFailureListener {
                                    val user = HomeAPI.user.value ?: return@addOnFailureListener
                                    // Email
                                    if (user.email != null) {
                                        navController.navigate(ProfileScreen.Select.route)
                                    }
                                    // Phone
                                    if (user.phone != null) {
                                        navController.navigate(ProfileScreen.Code.route)
                                        phone.signInVerificationId(phoneNumber = "${user.phone}")
                                    }
                                }
                            showDelete = false
                        }
                    ) {
                        Text(
                            text = "Yes",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDelete = false
                        }
                    ) {
                        Text(
                            text = "No",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                properties = DialogProperties(dismissOnClickOutside = false)
            )
        }
    }
}
@Composable
fun Mode() {
    var mode by remember { HomeAPI.mode }
    val items = listOf("Landlord", "Tenant")
    TabRow(
        modifier = Modifier.padding(horizontal = 20.dp),
        selectedTabIndex = mode
    ) {
        items.forEachIndexed { index, item ->
            Tab(
                selected = mode == index,
                onClick = { mode = index },
                text = {
                    Text(
                        text = item,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }
}
@Composable
fun Option(
    info: Boolean,
    image: Int,
    text: String,
    fontSize: Int,
    warning: Boolean,
    click: () -> Unit
) {
    Spacer(modifier = Modifier.padding(8.dp))
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            3.dp,
            if (info) Color.Transparent else MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(if (info) 40.dp else 80.dp)
            .padding(horizontal = 20.dp)
            .clickable {
                click()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (info) Arrangement.Center else Arrangement.Start
        ) {
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                imageVector = ImageVector.vectorResource(id = image),
                contentDescription = "Image",
                colorFilter = ColorFilter.tint(color = if (warning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface),
                modifier = Modifier
                    .size(if (info) 20.dp else 30.dp)
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = text,
                color = if (warning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold
            )
            if (info) {
                Spacer(modifier = Modifier.padding(30.dp))
            }
        }
    }
    Spacer(modifier = Modifier.padding(8.dp))
}