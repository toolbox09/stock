package com.example.stock.screens.login

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import com.example.stock.screens.root.RootViewModel
import com.example.stock.components.RoundedOutlinedTextField
import com.example.stock.components.IconSet

@Composable
fun LoginScreen( rootViewModel : RootViewModel ) {
    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by rootViewModel.state.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Text("로그인", style = MaterialTheme.typography.headlineMedium )

            IconSet.Logo(modifier = Modifier.size(96.dp)){}
            Spacer(Modifier.height(48.dp))
            RoundedOutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("아이디") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            RoundedOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { rootViewModel.login(id, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("로그인")
                }
            }
            state.error?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

}