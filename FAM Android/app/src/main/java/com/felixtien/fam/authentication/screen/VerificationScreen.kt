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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.felixtien.fam.R
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.PhoneException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    navController: NavHostController
) {
    val numbers = remember { mutableStateListOf(
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf(""),
        mutableStateOf("")
    ) }
    val focuses = remember { List(6) { FocusRequester() } }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            painter = painterResource(id = R.drawable.verification),
            contentDescription = "FAM",
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            for (i in numbers.indices){
                val prevFocus = if (i == 0) focuses[0] else focuses[i - 1]
                val currFocus = focuses[i]
                val nextFocus = if (i == 5) focuses[5] else focuses[i + 1]
                SMS(num = numbers[i].value,
                    prev = prevFocus,
                    curr = currFocus,
                    next = nextFocus,
                    onNumberTyped = {
                        if (it.count() <= 1) {
                            numbers[i].value = it
                        }else if (it.count() == 6){
                            for (j in it.indices){
                                numbers[j].value = it[j].toString()
                            }
                        }
                    })
                if (i != 5){
                    Spacer(modifier = Modifier.weight(0.2f))
                }
            }
        }
        Spacer(modifier = Modifier.weight(0.3f))
        Column(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            if (AuthAPI.progress) {
                CircularProgressIndicator()
            }
        }
        Spacer(modifier = Modifier.weight(0.6f))
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
            onClick = {
                val smsCode = numbers.joinToString(separator = "") { it.value }
                if (smsCode.count() != 6){
                    error = "Invalid SMS code."
                    return@ElevatedButton
                }
                AuthAPI.signInPhone(smsCode = smsCode) { errorMessage ->
                    if (errorMessage == PhoneException.INCORRECT){
                        error = "Incorrect SMS code."
                        AuthAPI.progress = false
                    }
                }
                AuthAPI.progress = true
            }
        ) {
            Text(
                text = "Verify", fontSize = 20.sp
            )
        }
        LaunchedEffect(Unit) {
            focuses[0].requestFocus()
            AuthAPI.progress = false
        }
    }
}
@Composable
fun SMS(
    num: String,
    prev: FocusRequester,
    curr: FocusRequester,
    next: FocusRequester,
    onNumberTyped: (String) -> Unit
) {
    val size = 50.dp
    val keyboard = LocalSoftwareKeyboardController.current
    val focus = LocalFocusManager.current
    LaunchedEffect(num) {
        if (num.count() == 1) {
            if (curr != next)
                next.requestFocus()
            else {
                keyboard?.hide()
                focus.clearFocus()
            }
        } else if (num.count() == 6) {
            focus.clearFocus()
            keyboard?.hide()
        }
        if (num.isEmpty() && curr != prev) {
            prev.requestFocus()
        }
    }
    OutlinedTextField(
        value = num,
        onValueChange = {
            onNumberTyped(
                if (it.isDigitsOnly()) {
                    if (it.count() < 3) {
                        if (it.isEmpty()) {
                            ""
                        } else {
                            if (it.first().toString() == num) {
                                it.last().toString()
                            } else {
                                it.first().toString()
                            }
                        }
                    } else if (it.count() == 6) {
                        it
                    } else {
                        ""
                    }
                } else {
                    ""
                }
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            focusedTextColor = Color.Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = if (curr != next) ImeAction.Next else ImeAction.Done
        ),
        textStyle = TextStyle(textAlign = TextAlign.Center),
        modifier = Modifier
            .focusRequester(curr)
            .background(Color.White)
            .width(size)
    )
}