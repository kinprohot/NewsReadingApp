package com.coding.meet.newsapp.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.ui.unit.Dp
import com.coding.meet.newsapp.data.model.Article
import com.coding.meet.newsapp.data.model.NewsResponse
import com.coding.meet.newsapp.data.model.Source
import com.coding.meet.newsapp.ui.navigation.NavigationItem
import com.coding.meet.newsapp.ui.navigation.Route
import newsreadingapp.app.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import kotlin.random.Random

const val BASE_URL = "https://newsapi.org/v2/"
const val DB_Name = "myNewsDB"
const val dataStoreFileName = "setting.preferences_pb"
val categoryList = arrayListOf(
    "Business",
    "Entertainment",
    "General",
    "Health",
    "Science",
    "Sports",
    "Technology"
)
val navigationItemsLists = listOf(
    NavigationItem(
        icon = Res.drawable.ic_headline,
        title = Res.string.headlines,
        route = Route.Headline
    ),
    NavigationItem(
        icon = Res.drawable.ic_search,
        title = Res.string.search,
        route = Route.Search,
    ),
    NavigationItem(
        icon = Res.drawable.ic_bookmark_outlined,
        title = Res.string.bookmark,
        route = Route.Bookmark,
    ),
)

enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}

enum class Type {
    Mobile, Desktop
}

data class Size(
    val width: Dp,
    val height: Dp
)

val articles: List<Article> = listOf(
    Article(
        source = Source("dwa", "My news"),
        author = "The author",
        title = "This is the main news title headline. This is the main news title headline.",
        description = "This is the main news description. This is the main news description. This is the main news description",
        url = "",
        urlToImage = "https://www.marketscreener.com/images/reuters/2024-03-05T144855Z_1_LYNXNPEK240IP_RTROPTP_3_GERMANY-TESLA-FIRE.JPG",
        publishedAt = Random(100).nextInt().toString(),
        content = "What is the content?"
    ),
    Article(
        source = Source("dawdwa", "My news"),
        author = "The author",
        title = "This is the main news title headline. This is the main news title headline.",
        description = "This is the main news description. This is the main news description. This is the main news description",
        url = "",
        urlToImage = "https://www.marketscreener.com/images/reuters/2024-03-05T144855Z_1_LYNXNPEK240IP_RTROPTP_3_GERMANY-TESLA-FIRE.JPG",
        publishedAt = Random(100).nextInt().toString(),
        content = "What is the content?"
    ),
    Article(
        source = Source("dwakjyk", "My news"),
        author = "The author",
        title = "This is the main news title headline. This is the main news title headline.",
        description = "This is the main news description. This is the main news description. This is the main news description",
        url = "",
        urlToImage = "https://www.marketscreener.com/images/reuters/2024-03-05T144855Z_1_LYNXNPEK240IP_RTROPTP_3_GERMANY-TESLA-FIRE.JPG",
        publishedAt = Random(100).nextInt().toString(),
        content = "What is the content?"
    ),
    Article(
        source = Source("dwserfewa", "My news"),
        author = "The author",
        title = "This is the main news title headline. This is the main news title headline.",
        description = "This is the main news description. This is the main news description. This is the main news description",
        url = "",
        urlToImage = "https://www.marketscreener.com/images/reuters/2024-03-05T144855Z_1_LYNXNPEK240IP_RTROPTP_3_GERMANY-TESLA-FIRE.JPG",
        publishedAt = Random(100).nextInt().toString(),
        content = "What is the content?"
    )
)
val newsResponse = NewsResponse(
    articles,
    "dwe",
    5
)
val FadeIn = fadeIn(animationSpec = tween(220, delayMillis = 90)) +
        scaleIn(
            initialScale = 0.92f,
            animationSpec = tween(220, delayMillis = 90)
        )

val FadeOut = fadeOut(animationSpec = tween(90))