package com.adlugosz.dao

import com.adlugosz.domain.Account
import java.sql.Connection
import javax.sql.DataSource

interface AccountsDao {
    /**
     *  returns id of created account
     */
    fun createAccount(holder: String, initialBalance: Int = 0, conn: Connection? = null): Long
    fun findAll(): List<Account>
    fun runInTransaction(block: (conn: Connection) -> Unit)
}

class DefaultAccountsDao(private val dataSource: DataSource): AccountsDao {
    override fun createAccount(holder: String, initialBalance: Int, conn: Connection?): Long {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Account> {
        TODO("Not yet implemented")
    }

    override fun runInTransaction(block: (conn: Connection) -> Unit) {
        dataSource.connection.use {
            it.autoCommit = false
            block(it)
        }
    }

}