package ss.fortberg.httpclient.model

data class AuthResponse(
    val token: String,
    val type: String,
    val resultCode: Int,
    val messageTitle: String,
    val messageDetail: String
)