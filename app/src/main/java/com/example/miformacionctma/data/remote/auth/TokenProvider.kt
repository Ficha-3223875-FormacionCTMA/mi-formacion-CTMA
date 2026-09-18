package com.example.miformacionctma.data.remote.auth

/**

 * Proveedor de tokens para la autenticación en las peticiones de red.
 * En una implementación real, esto podría obtener el token de DataStore o SharedPreferences.
 */
object TokenProvider {
    fun getToken(): String? {
        // Implementación placeholder. Retornar null o un token de prueba.
        return null
    }

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
