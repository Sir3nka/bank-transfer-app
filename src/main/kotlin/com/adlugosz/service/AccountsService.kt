package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.adlugosz.domain.Account
import com.github.michaelbull.result.runCatching

class AccountsService(private val accountsDao: AccountsDao) {
    fun createAccount(holder: String, initialBalance: Int = 0) = runCatching {
        accountsDao.createAccount(holder, initialBalance)
    }

    fun transferFunds(from: Long, to: Long) = runCatching {
        accountsDao.runInTransaction {
            accountsDao.createAccount("test", 0, it)
        }
    }

    fun getAllAcounts(): List<Account> {
        TODO("Not yet implemented")
    }

}
