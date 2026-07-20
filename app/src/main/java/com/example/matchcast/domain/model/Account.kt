package com.example.matchcast.domain.model

import android.net.Uri


data class Account(
    val name: String?,
    val email: String?,
    val photo: Uri?
)
