package com.example.authapp2.ui.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authapp2.AppRoutes
import com.example.authapp2.ui.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // Launcher para o fluxo de login com Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken!!
                isLoading = true
                viewModel.loginWithGoogle(idToken) { success ->
                    isLoading = false
                    if (success) {
                        navController.navigate(AppRoutes.HOME_SCREEN) {
                            popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Falha no login com Google.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: ApiException) {
                isLoading = false
                Toast.makeText(context, "Erro no Google Sign-In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo ao AuthApp!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    isLoading = true
                    viewModel.login(email, password) { success ->
                        isLoading = false
                        if (success) {
                            navController.navigate(AppRoutes.HOME_SCREEN) {
                                popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Usuário ou senha inválida.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val signInClient = viewModel.getGoogleSignInClient(context)
                    googleSignInLauncher.launch(signInClient.signInIntent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("G Entrar com Google")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString("Criar Conta"),
            onClick = { navController.navigate(AppRoutes.REGISTER_SCREEN) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        ClickableText(
            text = AnnotatedString("Esqueci minha senha"),
            onClick = { navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN) }
        )
    }
}