package com.coding.meet.newsapp.ui.bookmark

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.coding.meet.newsapp.ui.common.ArticleListScreen
import com.coding.meet.newsapp.ui.common.EmptyContent
import com.coding.meet.newsapp.ui.common.ShimmerEffect
import com.coding.meet.newsapp.ui.navigation.Route
import com.coding.meet.newsapp.utils.navigationItemsLists
import newsreadingapp.app.generated.resources.Res
import newsreadingapp.app.generated.resources.ic_browse
import newsreadingapp.app.generated.resources.no_bookmarks
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues
) {
    val bookmarkViewModel = koinViewModel<BookmarkViewModel>()

    val uiState by bookmarkViewModel.bookmarkNewsStateFlow.collectAsState()
    val originDirection = LocalLayoutDirection.current
    Column(
        modifier = Modifier.fillMaxSize().padding(
            start = paddingValues.calculateStartPadding(originDirection),
            end = paddingValues.calculateEndPadding(originDirection),
            bottom = paddingValues.calculateBottomPadding(),
        ),
    ) {
        TopAppBar(title = {
            Text(
                text = stringResource(navigationItemsLists[2].title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }, actions = {
            IconButton(onClick = {
                rootNavController.navigate(Route.SettingDetail)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                )
            }
        })
        uiState.DisplayResult(onLoading = {
            ShimmerEffect()
        }, onSuccess = { articleList ->
            if (articleList.isEmpty()) {
                EmptyContent(
                    message = stringResource(Res.string.no_bookmarks),
                    icon = Res.drawable.ic_browse, isOnRetryBtnVisible = false
                )
            } else {
                ArticleListScreen(
                    articleList = articleList,
                    rootNavController = rootNavController
                )
            }
        }, onError = {
            EmptyContent(message = it, icon = Res.drawable.ic_browse, onRetryClick = {
                bookmarkViewModel.getArticles()
            })
        })
    }

}