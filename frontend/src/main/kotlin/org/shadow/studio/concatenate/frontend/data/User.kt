package org.shadow.studio.concatenate.frontend.data

data class User (
    var userName: String,
    val uuid: String,
    val token: String,
    val refreshToken: String,
    val loginType: String
    )

