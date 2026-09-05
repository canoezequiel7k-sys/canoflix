package com.arigondev.canoflix.domain.model

import android.R

data class UserAccount(
    val email: String,
    val password: String,
    val name: String = "Usuario CanoFlix"
)
