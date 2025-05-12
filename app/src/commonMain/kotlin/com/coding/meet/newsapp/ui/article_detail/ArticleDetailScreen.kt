package com.coding.meet.newsapp.ui.article_detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.coding.meet.newsapp.data.model.Article
import com.coding.meet.newsapp.theme.mediumPadding
import com.coding.meet.newsapp.theme.xLargePadding
import com.coding.meet.newsapp.ui.common.PulseAnimation
import com.coding.meet.newsapp.utils.shareLink
import newsreadingapp.app.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    navController: NavController,
    currentArticle: Article
) {
    val articleDetailViewModel = koinViewModel<ArticleDetailViewModel>()
    LaunchedEffect(Unit) {
        articleDetailViewModel.isArticleBookmark(currentArticle)
    }
    val url = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(Res.string.news_detail),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    IconButton(onClick = {
                        shareLink(currentArticle.url)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = {
                        url.openUri(currentArticle.url)
                    }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_browse),
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = {
                        articleDetailViewModel.bookmarkArticle(currentArticle)
                    }) {
                        Icon(
                            painter = painterResource(
                                if (articleDetailViewModel.isBookmarked) Res.drawable.ic_bookmark_filled
                                else Res.drawable.ic_bookmark_outlined
                            ),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = xLargePadding),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(mediumPadding)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10)),
                    contentAlignment = Alignment.Center
                ) {
                    var imageLoadResult by remember {
                        mutableStateOf<Result<Painter>?>(null)
                    }
                    val painter = rememberAsyncImagePainter(
                        model = currentArticle.urlToImage,
                        onSuccess = {
                            imageLoadResult =
                                if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                                    Result.success(it.painter)
                                } else {
                                    Result.failure(Exception("Invalid image size"))
                                }
                        },
                        onError = {
                            it.result.throwable.printStackTrace()
                            imageLoadResult = Result.failure(it.result.throwable)
                        }
                    )

                    val painterState by painter.state.collectAsState()
                    val transition by animateFloatAsState(
                        targetValue = if (painterState is AsyncImagePainter.State.Success) {
                            1f
                        } else {
                            0f
                        },
                        animationSpec = tween(durationMillis = 800)
                    )

                    when (val result = imageLoadResult) {
                        null -> {
                            PulseAnimation(
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            Image(
                                painter = if (result.isSuccess) painter else {
                                    painterResource(Res.drawable.logo)
                                },
                                contentDescription = currentArticle.title,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .graphicsLayer {
                                        if (result.isSuccess) {
                                            rotationX = (1f - transition) * 30f
                                            val scale = 0.8f + (0.2f * transition)
                                            scaleX = scale
                                            scaleY = scale
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = currentArticle.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            currentArticle.description?.let { content ->
                item {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            currentArticle.publishedAt.let { content ->
                item {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

