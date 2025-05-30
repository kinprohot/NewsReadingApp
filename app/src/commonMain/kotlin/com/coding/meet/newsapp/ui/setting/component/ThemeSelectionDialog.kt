package com.coding.meet.newsapp.ui.setting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coding.meet.newsapp.theme.mediumPadding
import com.coding.meet.newsapp.theme.xLargePadding
import com.coding.meet.newsapp.theme.xSmallPadding
import com.coding.meet.newsapp.utils.Theme
import newsreadingapp.app.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectionDialog(
    onThemeChange: (Theme) -> Unit, onDismissRequest: () -> Unit, currentTheme: String
) {

    var currentSelectedTheme by remember { mutableStateOf(Theme.valueOf(currentTheme)) }

    BasicAlertDialog(onDismissRequest = onDismissRequest, content = {
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(mediumPadding)) {

                Text(
                    text = stringResource(Res.string.choose_a_theme),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(xSmallPadding)
                )
                Theme.entries.forEach { theme ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable {
                            currentSelectedTheme = theme
                        },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentSelectedTheme == theme,
                            onClick = { currentSelectedTheme = theme },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = stringResource(theme.title))
                    }
                }

                Spacer(modifier = Modifier.height(xLargePadding))

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(Res.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(mediumPadding))
                    TextButton(onClick = { onThemeChange(currentSelectedTheme) }) {
                        Text(text = stringResource(Res.string.apply))
                    }
                }
            }
        }
    })
}


@Composable
fun BookmarkDialog(
    onDeleteHistory: () -> Unit, onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(Res.string.delete_bookmark)) },
        text = { Text(stringResource(Res.string.delete_bookmark_description)) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(Res.string.delete_bookmark)
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteHistory()
                }
            ) {
                Text(stringResource(Res.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}
