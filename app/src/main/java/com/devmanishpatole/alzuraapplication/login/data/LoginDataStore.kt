package com.devmanishpatole.alzuraapplication.login.data

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginDataStore @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        val USER_LOGGED_IN = preferencesKey<Boolean>("user_looged_in")
        val USER_TOKEN = preferencesKey<String>("user_token")
    }

    private val dataStore: DataStore<Preferences> =
        context.createDataStore(name = "user")

    val userDataFlow: Flow<UserData> = dataStore.data
        .map { preferences ->
            val loggedIn = preferences[USER_LOGGED_IN] ?: false
            UserData(loggedIn)
        }

    val tokenFlow: Flow<Token> = dataStore.data
        .map { preferences ->
            val token = preferences[USER_TOKEN] ?: ""
            Token(token)
        }

    suspend fun updateUserStatus(loggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[USER_LOGGED_IN] = loggedIn
        }
    }

    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }
}