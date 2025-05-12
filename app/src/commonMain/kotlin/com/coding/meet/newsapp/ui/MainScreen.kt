package com.coding.meet.newsapp.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues // Thêm import này nếu MainAppNavGraph cần
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coding.meet.newsapp.ui.navigation.NavigationItem
import com.coding.meet.newsapp.ui.navigation.NavigationSideBar
import com.coding.meet.newsapp.ui.navigation.NewsBottomNavigation
import com.coding.meet.newsapp.ui.navigation.graphs.MainAppNavGraph // Import MainAppNavGraph
import com.coding.meet.newsapp.ui.navigation.Route // Import Route
import com.coding.meet.newsapp.ui.setting.SettingViewModel
import com.coding.meet.newsapp.utils.navigationItemsLists

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(
    settingViewModel: SettingViewModel,
    appRootNavController: NavHostController // Nhận NavController từ RootNavGraph
) {
    val windowSizeClass = calculateWindowSizeClass()
    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }
    // NavController này sẽ quản lý các tab của BottomNav/SideBar
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRouteString by remember(navBackStackEntry) { // Đổi tên để tránh nhầm lẫn với Route sealed interface
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    // Tìm NavigationItem dựa trên currentRouteString (là qualifiedName)
    val currentNavigationItem by remember(currentRouteString) {
        derivedStateOf {
            navigationItemsLists.find {
                it.route::class.qualifiedName == currentRouteString
            }
        }
    }

    val isMainScreenContentVisible by remember(currentNavigationItem) { // Sửa logic này
        derivedStateOf {
            currentNavigationItem != null // Hiển thị nếu có một tab đang được chọn
        }
    }
    val isBottomBarVisible by remember(isMediumExpandedWWSC, currentNavigationItem) { // Sửa logic này
        derivedStateOf {
            !isMediumExpandedWWSC && currentNavigationItem != null
        }
    }

    MainScaffold(
        tabNavController = tabNavController, // Truyền NavController của tab
        appRootNavController = appRootNavController, // Truyền NavController gốc của app
        settingViewModel = settingViewModel,
        currentRouteString = currentRouteString,
        isMediumExpandedWWSC = isMediumExpandedWWSC,
        isBottomBarVisible = isBottomBarVisible,
        isMainScreenContentVisible = isMainScreenContentVisible, // Sửa lại tên biến
        onItemClick = { clickedNavigationItem ->
            val targetRoute = clickedNavigationItem.route::class.qualifiedName ?: return@MainScaffold
            tabNavController.navigate(targetRoute) {
                tabNavController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) { saveState = true }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
fun MainScaffold(
    tabNavController: NavHostController, // NavController cho các tab
    appRootNavController: NavHostController, // NavController từ RootNavGraph
    settingViewModel: SettingViewModel,
    currentRouteString: String?,
    isMediumExpandedWWSC: Boolean,
    isBottomBarVisible: Boolean,
    isMainScreenContentVisible: Boolean, // Sửa lại tên biến
    onItemClick: (NavigationItem) -> Unit,
) {
    Row {
        AnimatedVisibility(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            visible = isMediumExpandedWWSC && isMainScreenContentVisible, // Sửa lại tên biến
            // ... (enter/exit animations)
        ) {
            NavigationSideBar(
                items = navigationItemsLists,
                currentRoute = currentRouteString,
                onItemClick = onItemClick
            )
        }
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = isBottomBarVisible,
                    // ... (enter/exit animations)
                ) {
                    NewsBottomNavigation(
                        items = navigationItemsLists,
                        currentRoute = currentRouteString,
                        onItemClick = onItemClick
                    )
                }
            }
        ) { innerPadding ->
            // Gọi MainAppNavGraph với các NavController đúng
            MainAppNavGraph(
                navController = tabNavController,       // NavController cho các tab
                outerNavController = appRootNavController, // NavController từ RootNavGraph
                innerPadding = innerPadding,
                settingViewModel = settingViewModel
            )
        }
    }
}