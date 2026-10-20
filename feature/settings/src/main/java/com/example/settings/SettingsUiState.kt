package com.example.settings

import com.example.domain.model.ServerSettings

data class  ServerSettings(
    public final val server: String = "",
    public final val database: String = "",
    public final val username: String = "",
    public final val password: String = ""
)