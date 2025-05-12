package com.coding.meet.newsapp.data.auth.service // Hoặc một package data.firebase

import com.coding.meet.newsapp.data.auth.AuthResult
import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.NewUser
import com.coding.meet.newsapp.domain.auth.model.User
import com.coding.meet.newsapp.domain.auth.service.FirebaseAuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthServiceImpl(
    private val firebaseAuth: FirebaseAuth // Inject FirebaseAuth instance
) : FirebaseAuthService {

    private fun FirebaseUser?.toDomainUser(): User? {
        return this?.let {
            User(id = it.uid, email = it.email ?: "", username = it.displayName)
        }
    }

    override fun getAuthState(): Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser.toDomainUser())
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }


    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser.toDomainUser()
    }

    override suspend fun registerWithEmailPassword(newUser: NewUser): AuthResult<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(newUser.email, newUser.password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // (Tùy chọn) Cập nhật display name nếu bạn dùng username
                // val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(newUser.username).build()
                // firebaseUser.updateProfile(profileUpdates).await()
                AuthResult.Success(firebaseUser.toDomainUser()!!)
            } else {
                AuthResult.Error("Registration failed: User not created.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred during registration.")
        }
    }

    override suspend fun signInWithEmailPassword(credentials: Credentials): AuthResult<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(credentials.email, credentials.password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                AuthResult.Success(firebaseUser.toDomainUser()!!)
            } else {
                AuthResult.Error("Sign-in failed: User not found.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An unknown error occurred during sign-in.")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Failed to send password reset email.")
        }
    }

    override suspend fun signOut(): AuthResult<Unit> {
        return try {
            firebaseAuth.signOut()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Sign out failed.")
        }
    }
}