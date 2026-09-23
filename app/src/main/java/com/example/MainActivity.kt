package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.screens.ArticleDetailBottomSheet
import com.example.ui.screens.DefenseScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RaconScreen
import com.example.ui.screens.ReisAiChatScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.FlagRed
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ReisAmber
import com.example.ui.theme.YuzyilNavy

sealed class Screen(val title: String, val icon: ImageVector, val tag: String) {
    object Home : Screen("Gündem", Icons.Default.Newspaper, "tab_home")
    object Racon : Screen("Racon", Icons.Default.Bolt, "tab_racon")
    object Defense : Screen("Savunma", Icons.Default.FlightTakeoff, "tab_defense")
    object History : Screen("Tarih", Icons.Default.HistoryEdu, "tab_history")
    object ReisAi : Screen("Reis AI", Icons.Default.AutoAwesome, "tab_reis_ai")
    object Saved : Screen("Kayıtlar", Icons.Default.Bookmarks, "tab_saved")
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val isElderMode by viewModel.isElderTextMode.collectAsState()
                val activeDetailItem by viewModel.activeDetailItem.collectAsState()
                val savedItems by viewModel.savedItems.collectAsState()

                var currentTabIndex by rememberSaveable { mutableIntStateOf(0) }
                val tabs = listOf(
                    Screen.Home,
                    Screen.Racon,
                    Screen.Defense,
                    Screen.History,
                    Screen.ReisAi,
                    Screen.Saved
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = FlagRed,
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🇹🇷", fontSize = 14.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Türkiye Yüzyılı",
                                        fontWeight = FontWeight.Black,
                                        fontSize = if (isElderMode) 22.sp else 19.sp,
                                        color = Color.White
                                    )
                                }
                            },
                            actions = {
                                // Elder Font Size Toggle ("Baba Modu")
                                IconButton(
                                    onClick = { viewModel.toggleElderTextMode() },
                                    modifier = Modifier.testTag("topbar_elder_font_toggle")
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isElderMode) ReisAmber else Color.White.copy(alpha = 0.15f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (isElderMode) "A++" else "A+",
                                                color = if (isElderMode) Color.Black else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = YuzyilNavy
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp,
                            modifier = Modifier.testTag("main_bottom_nav")
                        ) {
                            tabs.forEachIndexed { index, screen ->
                                val isSelected = currentTabIndex == index
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = { currentTabIndex = index },
                                    icon = {
                                        if (screen == Screen.Saved && savedItems.isNotEmpty()) {
                                            BadgedBox(
                                                badge = {
                                                    Badge(containerColor = FlagRed) {
                                                        Text("${savedItems.size}")
                                                    }
                                                }
                                            ) {
                                                Icon(screen.icon, contentDescription = screen.title)
                                            }
                                        } else {
                                            Icon(screen.icon, contentDescription = screen.title)
                                        }
                                    },
                                    label = {
                                        Text(
                                            text = screen.title,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = if (isElderMode) 12.sp else 10.sp
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = FlagRed,
                                        selectedTextColor = FlagRed,
                                        indicatorColor = FlagRed.copy(alpha = 0.15f),
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag(screen.tag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTabIndex) {
                            0 -> HomeScreen(viewModel = viewModel)
                            1 -> RaconScreen(viewModel = viewModel)
                            2 -> DefenseScreen(viewModel = viewModel)
                            3 -> HistoryScreen(viewModel = viewModel)
                            4 -> ReisAiChatScreen(viewModel = viewModel)
                            5 -> SavedScreen(viewModel = viewModel)
                        }

                        // Detail Bottom Sheet
                        activeDetailItem?.let { item ->
                            ArticleDetailBottomSheet(
                                item = item,
                                viewModel = viewModel,
                                onDismiss = { viewModel.closeDetail() }
                            )
                        }
                    }
                }
            }
        }
    }
}
