package com.coding.meet.newsapp.data.auth

import com.coding.meet.newsapp.domain.auth.model.Credentials
import com.coding.meet.newsapp.domain.auth.model.ForgotPasswordResponse
import com.coding.meet.newsapp.domain.auth.model.NewUser
import com.coding.meet.newsapp.domain.auth.service.FirebaseAuthService // Import service mới
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService, // Thay AuthApi bằng FirebaseAuthService
    private val authLocalDataSource: AuthLocalDataSource
) : AuthRepository {

    // isAuthenticated giờ sẽ dựa vào getAuthState từ FirebaseAuthService
    // và có thể kết hợp với token đã lưu (tùy logic bạn muốn)
    override val isAuthenticated: Flow<Boolean> =
        firebaseAuthService.getAuthState().mapNotNull { it != null }
    // Hoặc bạn có thể muốn kiểm tra cả token đã lưu:
    // combine(firebaseAuthService.getAuthState(), authLocalDataSource.getToken()) { user, token ->
    //     user != null && token != null
    // }


    override suspend fun login(credentials: Credentials): AuthResult<Unit> {
        val result = firebaseAuthService.signInWithEmailPassword(credentials)
        return when (result) {
            is AuthResult.Success -> {
                // Giả sử Firebase Auth tự quản lý session, bạn có thể không cần lưu token riêng
                // Nếu bạn VẪN MUỐN lưu token (ví dụ để dùng với API backend khác),
                // bạn cần `AuthResult.Success<User>` trả về token hoặc lấy token từ `FirebaseUser`
                // và sau đó gọi authLocalDataSource.saveToken(...)
                // Hiện tại, AuthResult.Success(Unit) là đủ nếu chỉ dựa vào Firebase session.
                // Để đơn giản, chúng ta có thể lấy token từ FirebaseUser (nếu có) và lưu.
                // Tuy nhiên, Firebase SDK tự xử lý việc duy trì trạng thái đăng nhập.
                // Nếu bạn cần token cho backend riêng:
                // firebaseAuthService.getCurrentUser()?.let { user -> /* lấy token từ user.idToken */ }
                AuthResult.Success(Unit)
            }
            is AuthResult.Error -> AuthResult.Error(result.message)
            AuthResult.Loading -> AuthResult.Loading // Nên xử lý trạng thái này nếu có
        }
    }

    override suspend fun register(newUser: NewUser): AuthResult<Unit> {
        val result = firebaseAuthService.registerWithEmailPassword(newUser)
        return when (result) {
            is AuthResult.Success -> AuthResult.Success(Unit) // Tương tự login, Firebase quản lý session
            is AuthResult.Error -> AuthResult.Error(result.message)
            AuthResult.Loading -> AuthResult.Loading
        }
    }

    override suspend fun forgotPassword(email: String): AuthResult<ForgotPasswordResponse> {
        // FirebaseAuthService.sendPasswordResetEmail trả về AuthResult<Unit>
        // Chúng ta cần điều chỉnh cho phù hợp hoặc AuthRepository không cần trả về ForgotPasswordResponse
        return when (val result = firebaseAuthService.sendPasswordResetEmail(email)) {
            is AuthResult.Success -> AuthResult.Success(ForgotPasswordResponse("Password reset email sent.", true)) // Tạo response giả
            is AuthResult.Error -> AuthResult.Error(result.message)
            AuthResult.Loading -> AuthResult.Loading
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        val result = firebaseAuthService.signOut()
        return when (result) {
            is AuthResult.Success -> {
                authLocalDataSource.clearToken() // Xóa token đã lưu (nếu có)
                AuthResult.Success(Unit)
            }
            is AuthResult.Error -> AuthResult.Error(result.message)
            AuthResult.Loading -> AuthResult.Loading
        }
    }
}