package com.example.authapp2.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.authapp2.AppRoutes
import com.example.authapp2.viewmodel.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: AuthViewModel) {
    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getUserName { name ->
            userName = name
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userName == null) {
            Text("Bem-vindo, Carregando...!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            Text("Bem-vindo, $userName!", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            viewModel.logout()
            navController.navigate(AppRoutes.LOGIN_SCREEN) {
                popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
            }
        }) {
            Text("Sair")
        }
    }
}