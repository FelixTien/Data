package com.felixtien.fam.authentication.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.Email
import com.felixtien.fam.authentication.EmailException
import com.felixtien.fam.authentication.graph.AuthScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavHostController,
    email: Email
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var username by remember { mutableStateOf("") }
        var mail by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirm by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }
        var exception by remember { mutableStateOf(false) }

        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        }, title = {
            Text("Home")
        })
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = "FAM",
            modifier = Modifier.size(250.dp)
        )
        InputWelcome(username = username, mail = mail, password = password, confirm = confirm, exception = exception){ newUserName, newMail, newPassword, newConfirm ->
            username = newUserName
            mail = newMail
            password = newPassword
            confirm = newConfirm
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress) {
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
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

                email.signUp(mail = mail, password = password, confirm = confirm){ errorMessage ->
                    if (errorMessage == null){
                        navController.popBackStack()
                        navController.navigate(AuthScreen.Create.route)
                    }else{
                        username = ""
                        mail = ""
                        password = ""
                        confirm = ""
                        exception = true
                        error = when (errorMessage) {
                            EmailException.EMPTY -> {
                                "No empty field."
                            }
                            EmailException.INVALID -> {
                                "Invalid email."
                            }
                            EmailException.LENGTH -> {
                                "Password should be at least 6 characters."
                            }
                            EmailException.PASSWORD -> {
                                "Password does not match."
                            }
                            EmailException.EXIST -> {
                                "The account has been registered."
                            }
                            else -> {
                                "Sever error."
                            }
                        }
                    }
                }
                AuthAPI.progress = false
            }
        ) {
            Text(
                text = "Register", fontSize = 20.sp
            )
        }

    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputWelcome(username: String, mail: String, password: String, confirm: String, exception: Boolean, binding: (String, String, String, String) -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current

    val (focusUserName, focusEmail, focusPassword, focusConfirm) = remember { FocusRequester.createRefs() }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = username,
        onValueChange = { binding(it, mail, password, confirm) },
        placeholder = { Text(text = "User Name") },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            errorTextColor = Color.Gray,
            cursorColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusEmail.requestFocus() }
        ),
        modifier = Modifier
            .background(Color.White)
            .focusRequester(focusUserName),
        isError = exception
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        value = mail,
        onValueChange = { binding(username, it, password, confirm) },
        placeholder = { Text(text = "Email") },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            errorTextColor = Color.Gray,
            cursorColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusPassword.requestFocus() }
        ),
        modifier = Modifier
            .background(Color.White)
            .focusRequester(focusEmail),
        isError = exception
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { binding(username, mail, it, confirm) },
        placeholder = { Text(text = "Password") },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                imageVector = if (passwordVisibility) ImageVector.vectorResource(id = R.drawable.visibility_off) else ImageVector.vectorResource(id = R.drawable.visibility),
                contentDescription = "Visibility",
                modifier = Modifier.clickable { passwordVisibility = !passwordVisibility },
                tint = Color.Gray
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            errorTextColor = Color.Gray,
            cursorColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusConfirm.requestFocus() }
        ),
        modifier = Modifier
            .background(Color.White)
            .focusRequester(focusPassword),
        isError = exception
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        value = confirm,
        onValueChange = { binding(username, mail, password, it) },
        placeholder = { Text(text = "Confirm Password") },
        visualTransformation = if (confirmVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                imageVector = if (confirmVisibility) ImageVector.vectorResource(id = R.drawable.visibility_off) else ImageVector.vectorResource(id = R.drawable.visibility),
                contentDescription = "Visibility",
                modifier = Modifier.clickable { confirmVisibility = !confirmVisibility },
                tint = Color.Gray
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            errorTextColor = Color.Gray,
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
            .background(Color.White)
            .focusRequester(focusConfirm),
        isError = exception
    )
}