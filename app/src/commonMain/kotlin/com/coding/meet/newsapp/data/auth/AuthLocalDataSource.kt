package com.coding.meet.newsapp.data.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow // Thêm import

interface AuthLocalDataSource {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun clearToken()
}

class FakeAuthLocalDataSource : AuthLocalDataSource {
    private var token: String? = null
    private val _tokenFlow = MutableStateFlow<String?>(null)

    init {
        _tokenFlow.value = token
    }

    override suspend fun saveToken(token: String) {
        this.token = token
        _tokenFlow.value = token
    }

    override fun getToken(): Flow<String?> {
        return _tokenFlow
    }

    override suspend fun clearToken() {
        token = null
        _tokenFlow.value = null
    }
}