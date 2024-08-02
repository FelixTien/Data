package com.felixtien.fam.home.dashboard.sub.landlord.screen

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.felixtien.fam.Navigation
import com.felixtien.fam.R
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.updateLeasePicture
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLeaseScreen(
    navController: NavHostController,
    index: Int
) {
    var leasename by remember { mutableStateOf(HomeAPI.leases[index].lease?.title ?: "Not set") }
    val focus = LocalFocusManager.current
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

                HomeAPI.launcher = updateLeasePicture(index = index) { HomeAPI.leases[index].lease?.url?.value = it }
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
                value = leasename,
                onValueChange = { leasename = it },
                label = { Text(text = "Lease Name") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focus.clearFocus()
                    }
                ),
            )
            Spacer(modifier = Modifier.height(35.dp))




            TypeField(index = index)





        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "The system starts to send reminding messages 5 days before the due day.",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
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
                    HomeAPI.leases[index].lease?.title = leasename
                    val title = HomeAPI.leases[index].lease?.title ?: ""
                    val type = HomeAPI.leases[index].lease?.type ?: ""
                    HomeAPI.updateLease(index = index, title = title, type = type){
                        navController.popBackStack()
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeField(index: Int) {
    val focus = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    val list = arrayOf("Due every 5th", "Due every 15th", "Due every 25th")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = HomeAPI.leases[index].lease?.type ?: "",
            onValueChange = {},
            label = {
                Text(text = "Due Date Type")
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = Color.Gray,
                focusedBorderColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(150.dp),
        ) {
            list.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        HomeAPI.leases[index].lease?.type = it
                        focus.clearFocus()
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditLeaseScreenPreview() {
    EditLeaseScreen(navController = rememberNavController(), index = 20)
}