package com.adlugosz.dao

import com.adlugosz.domain.Account
import javax.sql.DataSource

interface AccountsDao {
    /**
     *  returns id of created account
     */
    fun createAccount(holder: String, initialBalance: Int = 0): Long
    fun findAll(): List<Account>
}

class DefaultAccountsDao(private val dataSource: DataSource): AccountsDao {
    override fun createAccount(holder: String, initialBalance: Int): Long {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Account> {
        TODO("Not yet implemented")
    }

}