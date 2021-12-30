package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.adlugosz.domain.Account
import com.github.michaelbull.result.runCatching

class AccountsService(private val accountsDao: AccountsDao) {
    fun createAccount(holder: String, initialBalance: Int = 0) = runCatching {
        accountsDao.createAccount(holder, initialBalance)
    }

    fun getAllAcounts(): List<Account> {
        TODO("Not yet implemented")
    }

}
