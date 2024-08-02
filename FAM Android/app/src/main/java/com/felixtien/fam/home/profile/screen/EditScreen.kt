package com.felixtien.fam.home.profile.screen

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.felixtien.fam.Navigation
import com.felixtien.fam.R
import com.felixtien.fam.home.DatabaseException
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.updateProfilePicture
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditScreen(
    navController: NavHostController
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var username by remember { mutableStateOf(HomeAPI.user.value?.username ?: "Not set") }
    var uid by remember { mutableStateOf(HomeAPI.user.value?.uid ?: "Not set") }
    var error by remember { mutableStateOf("") }
    var exception by remember { mutableStateOf(false) }
    val (focusUserName, focusUid) = remember { FocusRequester.createRefs() }
    val focus = LocalFocusManager.current
    var url by remember { mutableStateOf(HomeAPI.user.value?.photoURL) }
    val cloud = Firebase.storage.reference

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            cloud.child(HomeAPI.user.value?.path ?: "").downloadUrl.addOnSuccessListener {
                url = it.toString()
            }
        }
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
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
            title = {}
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(210.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {}
            Box(
                modifier = Modifier.padding(vertical = 20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(contentAlignment = Alignment.Center) {
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

                HomeAPI.launcher = updateProfilePicture { url = it }
                Box(
                    modifier = Modifier
                        .offset(x = -(10).dp, y = -(10).dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { HomeAPI.imagePicker() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.camera),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        contentDescription = "Camera"
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "User Name") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusUid.requestFocus() }
                ),
                modifier = Modifier
                    .focusRequester(focusUserName),
                isError = exception
            )
            Spacer(modifier = Modifier.height(35.dp))
            OutlinedTextField(
                value = uid,
                onValueChange = { uid = it },
                label = { Text(text = "ID") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focus.clearFocus()
                        keyboard?.hide()
                    }
                ),
                modifier = Modifier
                    .focusRequester(focusUid),
                isError = exception
            )
        }
        Spacer(modifier = Modifier.weight(1f))
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                modifier = Modifier.weight(0.5f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondary),
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White
                )
            }
            Button(
                modifier = Modifier.weight(0.5f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    HomeAPI.updateProfile(username = username, uid = uid){ errorMessage ->
                        if (errorMessage == null){
                            HomeAPI.currentUser { HomeAPI.user.value = it }
                            navController.popBackStack()
                        }else{
                            exception = true
                            username = ""
                            uid = ""
                            error = when (errorMessage){
                                DatabaseException.EMPTY_USERNAME -> {
                                    "User name cannot be empty."
                                }
                                DatabaseException.NO_USER -> {
                                    "User not found."
                                }
                                DatabaseException.UID_EXIST -> {
                                    "ID has been used."
                                }
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Confirm",
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Navigation()
    }
}