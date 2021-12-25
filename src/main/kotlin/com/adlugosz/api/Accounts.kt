package com.adlugosz.api

class Accounts {
}

// for simplicity no floating types
data class CreateAccountRequest(val holder: String, val initialBalance: Int)