package com.coding.meet.newsapp.ui.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coding.meet.newsapp.data.model.Article // Giữ nguyên
import com.coding.meet.newsapp.ui.article_detail.ArticleDetailScreen // Giữ nguyên
import com.coding.meet.newsapp.ui.bookmark.BookmarkScreen // Giữ nguyên
import com.coding.meet.newsapp.ui.headline.HeadlineScreen // Giữ nguyên
import com.coding.meet.newsapp.ui.navigation.Route // Giữ nguyên (hoặc tên object Routes của bạn)
import com.coding.meet.newsapp.ui.search.SearchScreen // Giữ nguyên
import com.coding.meet.newsapp.ui.setting.SettingScreen // Giữ nguyên
import com.coding.meet.newsapp.ui.setting.SettingViewModel // Giữ nguyên
import kotlinx.serialization.json.Json // Giữ nguyên

@Composable
fun MainAppNavGraph(
    navController: NavHostController, // NavController cho SideBar
    outerNavController: NavHostController, // NavController của RootNavGraph
    innerPadding: PaddingValues, // Có thể không cần nếu dùng NavigationRail và Row
    settingViewModel: SettingViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Route.Headline::class.qualifiedName!!,
        modifier = Modifier.padding(innerPadding) // Áp dụng innerPadding nếu MainAppNavGraph được đặt trong Scaffold
            .fillMaxSize() // Thêm fillMaxSize để nội dung chiếm phần còn lại
    ) {
        composable(Route.Headline::class.qualifiedName!!) {
            HeadlineScreen(rootNavController = outerNavController, paddingValues = innerPadding) // Truyền outerNavController và innerPadding
        }
        composable(Route.Search::class.qualifiedName!!) {
            SearchScreen(rootNavController = outerNavController, paddingValues = innerPadding)
        }
        composable(Route.Bookmark::class.qualifiedName!!) {
            BookmarkScreen(rootNavController = outerNavController, paddingValues = innerPadding)
        }
        composable(Route.SettingDetail::class.qualifiedName!!) {
            SettingScreen(
                navController = navController, // Hoặc outerNavController tùy nhu cầu
                settingViewModel = settingViewModel
                // Thêm callback onLogout nếu cần
            )
        }
        // Route.NewsDetail sẽ được xử lý bởi RootNavGraph nếu nó là màn hình không có SideBar
    }
}