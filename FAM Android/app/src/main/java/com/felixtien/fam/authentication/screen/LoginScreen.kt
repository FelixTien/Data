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
import androidx.compose.material3.TextButton
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
import com.felixtien.fam.authentication.EmailException
import com.felixtien.fam.authentication.graph.AuthScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var exception by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            painter = painterResource(id = R.drawable.login),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        InputLogin(reAuth = false, mail = mail, password = password, exception = exception){ newMail, newPassword ->
            mail = newMail
            password = newPassword
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress){
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ElevatedButton(
                modifier = Modifier.weight(0.5f),
                onClick = {
                    navController.navigate(AuthScreen.Forgot.route)
                }
            ) {
                Text(text = "Reset Password")
            }
            ElevatedButton(
                modifier = Modifier.weight(0.5f),
                onClick = {
                    AuthAPI.progress = true
                    AuthAPI.signInEmail(mail = mail, password = password){ errorMessage ->
                        exception = true
                        mail = ""
                        password = ""
                        error = when (errorMessage){
                            EmailException.EMPTY -> {
                                "No empty field."
                            }
                            EmailException.VERIFY -> {
                                "Account has not yet been verified."
                            }
                            else -> {
                                "Invalid email or password."
                            }
                        }
                    }
                    AuthAPI.progress = false
                }
            ) {
                Text(text = "Log In")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        TextButton(
            modifier = Modifier.padding(bottom = 50.dp),
            onClick = {
                navController.popBackStack()
                navController.navigate(AuthScreen.Welcome.route)
            }
        ) {
            Text(text = "Register an account and join us.")
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputLogin(reAuth: Boolean, mail: String, password: String, exception: Boolean, binding: (String, String) -> Unit) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    val (focusEmail, focusPassword) = remember { FocusRequester.createRefs() }
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = mail,
        onValueChange = { binding(it, password) },
        placeholder = { Text(text = "Email") },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray,
            errorTextColor = Color.Gray,
            cursorColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            disabledTextColor = Color.Gray
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
        isError = exception,
        enabled = !reAuth
    )
    Spacer(modifier = Modifier.height(20.dp))
    OutlinedTextField(
        value = password,
        onValueChange = { binding(mail, it) },
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
            .focusRequester(focusPassword),
        isError = exception
    )
}