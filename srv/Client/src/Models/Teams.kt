package com.example.Data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


data class Teams(val id: Int, val name: String){
}

