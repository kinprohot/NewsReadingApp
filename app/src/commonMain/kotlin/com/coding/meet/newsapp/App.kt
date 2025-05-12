package com.coding.meet.newsapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.coding.meet.newsapp.theme.NewsAppTheme
import com.coding.meet.newsapp.ui.MainScreen
import com.coding.meet.newsapp.ui.auth.login.LoginScreen
import com.coding.meet.newsapp.ui.navigation.graphs.RootNavGraph
import com.coding.meet.newsapp.ui.setting.SettingViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        RootNavGraph()
    }
}