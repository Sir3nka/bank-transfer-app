package com.adlugosz.dao

import com.adlugosz.domain.Account
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

interface AccountsDao {
    /**
     *  returns id of created account
     */
    fun createAccount(holder: String, initialBalance: Int = 0, conn: Connection): Long
    fun findAll(): List<Account>
    fun <T> runInTransaction(block: (conn: Connection) -> T): T
    fun addCash(to: Long, cash: Int, conn: Connection): Int
    fun removeCash(to: Long, cash: Int, conn: Connection): Int
    fun checkBalance(id: Long, conn: Connection): Int
}

class DefaultAccountsDao(private val dataSource: DataSource) : AccountsDao {

    private val createAccount: String = "INSERT INTO Account (holder, balance) VALUES (?, ?)"
    private val getAccounts: String = "SELECT * FROM Account"
    private val addCash: String = "UPDATE Account SET balance = balance + ? where id = ?"
    private val removeCash: String = "UPDATE Account SET balance = balance - ? where id = ? AND balance >= ?"
    private val getCash: String = "SELECT * FROM Account where id = ? FOR UPDATE"

    override fun createAccount(holder: String, initialBalance: Int, conn: Connection): Long {
        return conn.prepareStatement(createAccount, Statement.RETURN_GENERATED_KEYS).apply {
            setString(1, holder)
            setInt(2, initialBalance)
        }.also { it.executeUpdate() }.generatedKeys
            .use { resultSet ->
                resultSet.takeIf { it.next() }?.getLong(1) ?: throw RuntimeException("Failed to acquire resource id")
            }
    }

    override fun findAll(): List<Account> {
        return dataSource.connection.use {
            it.prepareStatement(getAccounts).executeQuery {
                Account(it.getString("holder"), it.getInt("balance"))
            }.toList()
        }
    }

    private fun <T> PreparedStatement.executeQuery(
        target: MutableCollection<T> = mutableListOf(),
        mappingFunction: (result: ResultSet) -> T
    ): MutableCollection<T> {
        this.executeQuery().let {
            while (it.next()) {
                target.add(mappingFunction(it))
            }
        }
        return target
    }

    override fun <T> runInTransaction(block: (conn: Connection) -> T) =
        dataSource.connection.use { conn ->
            conn.autoCommit = false
            block(conn).also { conn.commit() }
        }

    override fun addCash(to: Long, cash: Int, conn: Connection): Int {
        return conn.prepareStatement(addCash).also {
            it.setInt(1, cash)
            it.setLong(2, to)
        }.executeUpdate()
    }

    override fun removeCash(to: Long, cash: Int, conn: Connection): Int {
        return conn.prepareStatement(removeCash).also {
            it.setInt(1, cash)
            it.setLong(2, to)
            it.setInt(3, cash)
        }.executeUpdate()
    }

    override fun checkBalance(id: Long, conn: Connection): Int {
        return conn.prepareStatement(getCash).also {
            it.setLong(1, id)
        }.executeQuery().let {
            it.takeIf { it.next() }?.getInt("balance") ?: 0
        }
    }
}