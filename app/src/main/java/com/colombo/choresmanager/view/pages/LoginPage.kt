package com.colombo.choresmanager.view.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.colombo.choresmanager.R
import com.colombo.choresmanager.viewmodels.ChoresOverviewViewModel

@Composable
fun LoginPage(
    viewModel: ChoresOverviewViewModel,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isAuthLoading.observeAsState(false)
    val authError by viewModel.authError.observeAsState(null)
    val rememberedUsers by viewModel.rememberedUsernames.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineSmall,
        )

        Text(
            text = stringResource(R.string.select_profile),
            style = MaterialTheme.typography.bodyMedium,
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.profile_guest),
                modifier = Modifier.clickable { viewModel.continueAsGuest() },
                color = MaterialTheme.colorScheme.primary,
            )
            rememberedUsers.forEach {
                Text(
                    text = it,
                    modifier = Modifier.clickable {
                        username = it
                        viewModel.clearAuthError()
                    },
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                viewModel.clearAuthError()
            },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                viewModel.clearAuthError()
            },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        )

        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        ) {
            Text(stringResource(R.string.login))
        }

        Button(
            onClick = { viewModel.register(username, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        ) {
            Text(stringResource(R.string.create_account))
        }

        Button(
            onClick = { viewModel.continueAsGuest() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        ) {
            Text(stringResource(R.string.continue_as_guest))
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        authError?.takeIf { it.isNotBlank() }?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
