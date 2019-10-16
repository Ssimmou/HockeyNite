package com.server.com.server.Models

import org.jetbrains.exposed.sql.Table



data class Players(val id: Int, val name: String, val teamId : Int)
