package com.example.authapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp2.data.AuthRepository
import com.example.authapp2.ui.theme.AuthApp2Theme
import com.example.authapp2.ui.view.ForgotPasswordScreen
import com.example.authapp2.ui.view.HomeScreen
import com.example.authapp2.ui.view.LoginScreen
import com.example.authapp2.ui.view.RegisterScreen
import com.example.authapp2.viewmodel.AuthViewModel
import com.example.authapp2.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthApp2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authRepository = AuthRepository()
                    val viewModelFactory = AuthViewModelFactory(authRepository)
                    val authViewModel: AuthViewModel = viewModel(factory = viewModelFactory)

                    AppNavigator(authViewModel)
                }
            }
        }
    }
}

object AppRoutes {
    const val LOGIN_SCREEN = "login"
    const val REGISTER_SCREEN = "register"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password"
    const val HOME_SCREEN = "home"
}

@Composable
fun AppNavigator(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    val startDestination = if (viewModel.isUserLogged()) {
        AppRoutes.HOME_SCREEN
    } else {
        AppRoutes.LOGIN_SCREEN
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppRoutes.LOGIN_SCREEN) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(navController = navController, viewModel = viewModel)
        }
        composable(AppRoutes.HOME_SCREEN) {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
    }
}