package com.coding.meet.newsapp.ui.navigation.graphs // Hoặc package của App.kt

// Import Res object từ thư viện tài nguyên của bạn (ví dụ: moko-resources)
// import news_reading_app.composeapp.generated.resources.* // Ví dụ với moko-resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coding.meet.newsapp.data.model.Article
import com.coding.meet.newsapp.domain.auth.usecase.CheckAuthenticationUseCase
import com.coding.meet.newsapp.theme.NewsAppTheme
import com.coding.meet.newsapp.ui.article_detail.ArticleDetailScreen
import com.coding.meet.newsapp.ui.auth.navigation.AuthRoutes
import com.coding.meet.newsapp.ui.auth.navigation.authGraph
import com.coding.meet.newsapp.ui.navigation.NavigationItem
import com.coding.meet.newsapp.ui.navigation.NavigationSideBar
import com.coding.meet.newsapp.ui.navigation.Route
import com.coding.meet.newsapp.ui.setting.SettingViewModel
import kotlinx.serialization.json.Json
import newsreadingapp.app.generated.resources.Res
import newsreadingapp.app.generated.resources.bookmark
import newsreadingapp.app.generated.resources.headlines
import newsreadingapp.app.generated.resources.ic_bookmark_outlined
import newsreadingapp.app.generated.resources.ic_headline
import newsreadingapp.app.generated.resources.ic_search
import newsreadingapp.app.generated.resources.ic_settings
import newsreadingapp.app.generated.resources.search
import newsreadingapp.app.generated.resources.setting
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import com.coding.meet.newsapp.ui.MainScreen


// Định nghĩa các route cho RootNavGraph
object RootAppScreen {
    const val AUTH_FLOW = AuthRoutes.ROOT
    const val MAIN_APP_CONTENT = "main_app_content_route"
    const val LOADING = "loading_route_root"
    // Định nghĩa route cho ArticleDetail nếu nó là màn hình riêng biệt không có SideBar
    fun articleDetailRoute(articleId: String?) = "${Route.NewsDetail::class.qualifiedName}/$articleId"
    const val ARTICLE_DETAIL_BASE_ROUTE = "news_detail_base_route" // Dùng cho composable definition
}

@Composable
fun RootNavGraph() {
    val rootNavController = rememberNavController()
    val checkAuthenticationUseCase: CheckAuthenticationUseCase = koinInject()
    val isAuthenticated by checkAuthenticationUseCase().collectAsState(initial = null)

    val settingViewModel: SettingViewModel = koinViewModel()
    val currentTheme by settingViewModel.currentTheme.collectAsState()

    NewsAppTheme(currentTheme) {
        val startDestination = remember(isAuthenticated) {
            when (isAuthenticated) {
                true -> RootAppScreen.MAIN_APP_CONTENT
                false -> RootAppScreen.AUTH_FLOW
                null -> RootAppScreen.LOADING
            }
        }

        if (startDestination == RootAppScreen.LOADING) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            NavHost(navController = rootNavController, startDestination = startDestination) {
                authGraph(
                    navController = rootNavController,
                    onAuthSuccess = {
                        rootNavController.navigate(RootAppScreen.MAIN_APP_CONTENT) {
                            popUpTo(RootAppScreen.AUTH_FLOW) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

                composable(RootAppScreen.MAIN_APP_CONTENT) {
                    MainScreen(
                        settingViewModel = settingViewModel,
                        appRootNavController = rootNavController
                    )
                }

                // Định nghĩa ArticleDetailScreen ở đây nếu nó không có SideBar
                // và được điều hướng từ một màn hình bên trong MainAppWithSideBar
                composable(
                    route = "${Route.NewsDetail::class.qualifiedName}/{articleJson}"
                ) { backStackEntry ->
                    val articleJson = backStackEntry.arguments?.getString("articleJson")
                    var articleToShow: Article? = null // Biến để lưu Article sau khi parse
                    var parseError: String? = null

                    if (articleJson != null) {
                        try {
                            articleToShow = Json.decodeFromString<Article>(articleJson)
                        } catch (e: Exception) {
                            // Ghi log lỗi nếu cần
                            // Log.e("Navigation", "Error parsing article JSON", e)
                            parseError = "Error loading article details. Invalid data."
                        }
                    } else {
                        parseError = "Article data not found."
                    }

                    // Bây giờ mới gọi Composable dựa trên kết quả
                    if (articleToShow != null) {
                        ArticleDetailScreen(navController = rootNavController, currentArticle = articleToShow)
                    } else {
                        // Hiển thị thông báo lỗi hoặc một UI thay thế
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(parseError ?: "Unknown error loading article.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppWithSideBar(rootNavController: NavHostController, settingViewModel: SettingViewModel) {
    val sideBarNavController = rememberNavController()

    val sideBarItems = remember {
        listOf(
            NavigationItem(
                // Sử dụng đối tượng Res để truy cập drawable và string
                icon = Res.drawable.ic_headline, // Ví dụ: Giả sử bạn có ic_headline.xml
                title = Res.string.headlines, // Ví dụ: Giả sử bạn có string "headline_title"
                route = Route.Headline
            ),
            NavigationItem(
                icon = Res.drawable.ic_search,
                title = Res.string.search,
                route = Route.Search
            ),
            NavigationItem(
                // Chọn một icon cho bookmark, ví dụ ic_bookmark_filled hoặc ic_bookmark_outlined
                icon = Res.drawable.ic_bookmark_outlined, // Hoặc ic_bookmark_outlined
                title = Res.string.bookmark,
                route = Route.Bookmark
            ),
            NavigationItem(
                icon = Res.drawable.ic_settings,      // Đảm bảo bạn có ic_settings.xml
                title = Res.string.setting,           // SỬ DỤNG TÊN CHUỖI TỪ strings.xml CỦA BẠN
                route = Route.SettingDetail
            )
        )
    }

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationSideBar(
            items = sideBarItems,
            currentRoute = sideBarNavController.currentBackStackEntryAsState().value?.destination?.route,
            onItemClick = { navigationItem ->
                val targetRoute = navigationItem.route::class.qualifiedName ?: return@NavigationSideBar
                sideBarNavController.navigate(targetRoute) {
                    popUpTo(sideBarNavController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        MainAppNavGraph(
            navController = sideBarNavController,
            outerNavController = rootNavController,
            innerPadding = PaddingValues(),
            settingViewModel = settingViewModel
        )
    }
}

// File: MainAppNavGraph.kt (từ NavGraph.kt cũ của bạn)
// Cần sửa đổi để nhận outerNavController và sử dụng qualifiedName cho route
