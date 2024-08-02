package com.felixtien.fam.home.dashboard.sub.landlord.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import com.felixtien.fam.home.HomeAPI
import com.felixtien.fam.home.Item
import com.felixtien.fam.home.dashboard.sub.landlord.graph.LandlordScreen
import kotlinx.coroutines.delay

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaseListScreen(
    navController: NavHostController
) {
    var showDelete by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf(Item(lease = null)) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(Unit) {
            HomeAPI.loadLease()
        }
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
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
            actions = {
                IconButton(onClick = {
                    HomeAPI.addLease()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Localized description"
                    )
                }
            },
            title = {}
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            itemsIndexed(
                items = HomeAPI.leases,
                key = { _, item -> item }
            ) { index, item ->
                LaunchedEffect(key1 = item.isRemoved.value) {
                    if (item.isRemoved.value){
                        delay(500.toLong())
                        HomeAPI.leases -= item
                    }
                }
                AnimatedVisibility(
                    visible = !item.isRemoved.value,
                    exit = shrinkVertically(
                        animationSpec = tween(durationMillis = 500),
                        shrinkTowards = Alignment.Top
                    ) + fadeOut()
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = item.lease?.title ?: "",
                                fontSize = 20.sp
                            )
                        },
                        leadingContent = {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {}
                                Box(
                                    contentAlignment = Alignment.Center,
                                ) {
                                    val url = HomeAPI.leases[index].lease?.url?.value.orEmpty()
                                    if (url.isEmpty()) {
                                        Image(
                                            painter = painterResource(id = R.drawable.lease),
                                            contentDescription = "Lease",
                                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.surface),
                                            modifier = Modifier.size(55.dp)
                                        )
                                    }else {
                                        CircularProgressIndicator(
                                            color = MaterialTheme.colorScheme.onError,
                                            modifier = Modifier.size(25.dp)
                                        )
                                        Image(
                                            painter = rememberAsyncImagePainter(HomeAPI.leases[index].lease?.url?.value),
                                            contentDescription = "Profile",
                                            modifier = Modifier
                                                .size(55.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                itemToDelete = item
                                showDelete = true
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .clickable {
                                navController.navigate(LandlordScreen.Lease.route + "/$index")
                            }
                    )
                }
                Divider()
            }
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
                        text = "Are you sure you want to delete ${itemToDelete.lease?.title}?",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            HomeAPI.deleteLease(id = itemToDelete.lease?.id ?: "")
                            itemToDelete.isRemoved.value = true
                            itemToDelete = Item(lease = null)
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
        Navigation()
    }
}

@Preview(showBackground = true)
@Composable
private fun LeaseListScreenPreview() {
    LeaseListScreen(navController = rememberNavController())
}