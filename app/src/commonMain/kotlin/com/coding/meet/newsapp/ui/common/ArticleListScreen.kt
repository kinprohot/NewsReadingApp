package com.coding.meet.newsapp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.coding.meet.newsapp.data.model.Article
import com.coding.meet.newsapp.theme.cardMinSize
import com.coding.meet.newsapp.theme.mediumPadding
import com.coding.meet.newsapp.ui.navigation.Route
import com.coding.meet.newsapp.utils.randomUUIDStr
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun ArticleListScreen(
    articleList: List<Article>,
    rootNavController: NavController
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(cardMinSize),
        verticalItemSpacing = mediumPadding,
        horizontalArrangement = Arrangement.spacedBy(mediumPadding),
        contentPadding = PaddingValues(mediumPadding),
    ) {
        items(articleList, key = {
            it.publishedAt + randomUUIDStr()
        }) { item ->
            ArticleItem(article = item, onClick = {

                val articleStr = Json.encodeToString(item)
                rootNavController.currentBackStackEntry?.savedStateHandle?.apply {
                    set("article", articleStr)
                }
                rootNavController.navigate(Route.NewsDetail)
            })
        }
    }

}