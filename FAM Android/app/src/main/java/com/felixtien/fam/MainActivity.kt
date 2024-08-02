package com.felixtien.fam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.felixtien.fam.authentication.AuthAPI
import com.felixtien.fam.authentication.Google
import com.felixtien.fam.authentication.graph.AuthGraph
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.HomeScreen
import com.felixtien.fam.ui.theme.FAMTheme
import com.felixtien.fam.R
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val google = Google(component = this)
        setContent {
            FAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (AuthAPI.signIn) {
                        HomeAPI.currentUser { user ->
                            if (user == null){
                                AuthAPI.signOut()
                                HomeAPI.problem.value = true
                                return@currentUser
                            }else {
                                HomeAPI.user = mutableStateOf(user)
                                HomeAPI.synchronize()
                            }
                        }
                        HomeScreen(
                            activity = this,
                            google = google
                        )
                    } else {
                        BackPic(outside = true)
                        AuthGraph(
                            navController = rememberNavController(),
                            activity = this,
                            google = google
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BackPic(outside: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = if (outside) R.drawable.background else R.drawable.background2),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
    }
}
@Composable
fun Navigation() {
    var tab by remember { HomeAPI.page }
    val items = listOf("Dashboard", "Management", "Profile")
    val icons = listOf(R.drawable.dashboard, R.drawable.management, R.drawable.profile)
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = icons[index]),
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = tab == index,
                onClick = {
                    HomeAPI.page.intValue = index
                    tab = index
                }
            )
        }
    }
}
