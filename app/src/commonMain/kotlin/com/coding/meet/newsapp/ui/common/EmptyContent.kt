package com.coding.meet.newsapp.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.coding.meet.newsapp.theme.imageSize
import com.coding.meet.newsapp.theme.smallPadding
import newsreadingapp.app.generated.resources.Res
import newsreadingapp.app.generated.resources.retry
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EmptyContent(
    message: String,
    icon: DrawableResource,
    isOnRetryBtnVisible: Boolean = true,
    onRetryClick: (() -> Unit) = { }
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(imageSize),
            painter = painterResource(icon),
            tint = if (!isSystemInDarkTheme()) LightGray else DarkGray,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(smallPadding),
            text = message,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = if (!isSystemInDarkTheme()) LightGray else DarkGray,
        )
        if (isOnRetryBtnVisible) {
            Button(onClick = onRetryClick) {
                Text(
                    text = stringResource(Res.string.retry),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}