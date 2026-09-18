package com.example.miformacionctma.data.remote.auth

/**
 * Gestiona el token de autenticación para las peticiones de red.
 * En una implementación real, esto vendría de DataStore o un Secure Storage.
 */
object TokenProvider {
    private var currentToken: String? = null

    fun setToken(token: String) {
        currentToken = token
    }

    fun getToken(): String? {
        return currentToken
    }

    fun clearToken() {
        currentToken = null
    }

    fun hasToken(): Boolean = currentToken != null
}
