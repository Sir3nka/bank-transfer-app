package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.adlugosz.domain.Account

class AccountsService(private val accountsDao: AccountsDao) {
    fun createAccount(holder: String, initialBalance: Int = 0 ): Result<Unit> {
        TODO("Not yet implemented")
    }

    fun getAllAcounts(): List<Account> {
        TODO("Not yet implemented")
    }

}
