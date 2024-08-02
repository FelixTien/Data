package com.felixtien.fam.authentication.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.Phone
import com.felixtien.fam.authentication.graph.AuthScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneScreen(
    navController: NavHostController,
    phone: Phone
) {
    var enable by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        phone.number = ""
        AuthAPI.progress = false
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            title = {
                Text("Home")
            }
        )
        Image(
            painter = painterResource(id = R.drawable.phone),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        PhoneNumber(phone = phone)
        Spacer(modifier = Modifier.weight(0.3f))
        Text(
            text = "We will send you a SMS message to confirm your phone number.",
            color = Color.Black,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.35f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.35f))
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-15).dp)
        )
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(bottom = 50.dp),
            enabled = enable,
            onClick = {
                if (phone.number.isEmpty()){
                    error = "Invalid phone number."
                    return@ElevatedButton
                }
                AuthAPI.progress = true
                enable = false
                navController.popBackStack()
                navController.navigate(AuthScreen.Verification.route)
                phone.signInVerificationId(phoneNumber = "${phone.country[phone.code]}${phone.number}")
            }
        ) {
            Text(
                text = "Continue",
                fontSize = 20.sp
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumber(phone: Phone) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }

    val list = arrayOf("US", "TW", "JP")
    val icon = mapOf(
        "US" to R.drawable.us,
        "TW" to R.drawable.taiwan,
        "JP" to R.drawable.japan
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier
                .width(110.dp)
        ) {
            OutlinedTextField(
                value = phone.country[phone.code] ?: "",
                onValueChange = {},
                placeholder = {
                    Text(text = phone.code)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedTextColor = Color.Gray,
                    focusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Gray
                ),
                leadingIcon = {
                    Image(
                        painter = painterResource(id = icon[phone.code] ?: 0),
                        contentDescription = "Icon Description",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )
                },
                readOnly = true,
                modifier = Modifier
                    .background(Color.White)
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
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = icon[it] ?: 0),
                                contentDescription = "Icon Description",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        onClick = {
                            phone.code = it
                            expanded = false
                        }
                    )
                }
            }
        }
        OutlinedTextField(
            value = phone.number,
            onValueChange = { phone.number = it },
            placeholder = { Text(text = "Phone Number")},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                unfocusedTextColor = Color.Gray,
                focusedBorderColor = Color.Gray,
                focusedTextColor = Color.Gray
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focus.clearFocus()
                    keyboard?.hide()
                }
            ),
            modifier = Modifier
                .background(Color.White)
                .weight(1f)
        )
    }
}