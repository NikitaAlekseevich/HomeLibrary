package com.example.homelibrary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.ui.viewmodel.AuthViewModel
import androidx.compose.ui.res.stringResource
import com.example.homelibrary.R

@Composable
fun SignUpScreen(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { authViewModel.signUp(email, password) }) {
            Text(stringResource(id = R.string.sign_up))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("signIn") }) {
            Text(stringResource(id = R.string.sign_in))
        }
    }

    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

    LaunchedEffect(isUserAuthenticated) {
        if (isUserAuthenticated) {
            navController.navigate("home") {
                popUpTo("signUp") { inclusive = true }
            }
        }
    }
}