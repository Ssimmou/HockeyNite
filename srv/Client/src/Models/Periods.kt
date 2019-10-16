package com.example.Data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction



data class Periods(val id: Int, val gameId: Int, val ended : Int) {
}


