package com.coding.meet.newsapp.domain.auth.service

import com.coding.meet.newsapp.data.auth.AuthResult // Sử dụng AuthResult đã định nghĩa
import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.NewUser
import com.coding.meet.newsapp.domain.auth.model.User // Domain model User
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthService {
    /**
     * Returns a flow that emits the current authenticated user, or null if no user is signed in.
     * This flow should update whenever the authentication state changes.
     */
    fun getAuthState(): Flow<User?>

    /**
     * Gets the current Firebase user, if one is signed in.
     * This might be a one-time check. For reactive updates, use getAuthState().
     */
    suspend fun getCurrentUser(): User?

    suspend fun registerWithEmailPassword(newUser: NewUser): AuthResult<User>
    suspend fun signInWithEmailPassword(credentials: Credentials): AuthResult<User>
    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit>
    suspend fun signOut(): AuthResult<Unit>

    // Thêm các phương thức khác nếu cần (Google Sign-In, Phone Auth, etc.)
    // suspend fun signInWithGoogle(idToken: String): AuthResult<User>
}