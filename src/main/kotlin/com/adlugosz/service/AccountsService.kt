package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching

class AccountsService(private val accountsDao: AccountsDao) {
    fun createAccount(holder: String, initialBalance: Int = 0) = runCatching {
        accountsDao.runInTransaction {
            accountsDao.createAccount(holder, initialBalance, it)
        }
    }

    fun transferFunds(from: Long, to: Long, cash: Int): Result<Unit, Throwable> = runCatching {
        accountsDao.runInTransaction { conn ->
            accountsDao.checkBalance(from, conn)
                .takeIf { it >= cash }?.let {
                    accountsDao.removeCash(from, cash, conn).throwIf(RuntimeException("Failed to update any rows")) { it == 0 }
                    accountsDao.addCash(to, cash, conn).throwIf(RuntimeException("Failed to update any rows")) { it == 0}
                }
        }
    }

    fun getAllAccounts() = accountsDao.findAll()

    private fun <T> T.throwIf(
        exception: RuntimeException = RuntimeException("${this.toString()} failed test"),
        predicate: () -> Boolean
    ) =
        if (predicate()) throw exception
        else this

}
