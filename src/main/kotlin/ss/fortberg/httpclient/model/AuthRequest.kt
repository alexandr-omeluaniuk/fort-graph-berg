package ss.fortberg.httpclient.model

data class AuthRequest(
    val pin: String,
    val rememberPin: Boolean = true,
    val returnToken: Boolean = true
)
