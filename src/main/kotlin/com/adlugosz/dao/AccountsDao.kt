package com.adlugosz.dao

import com.adlugosz.domain.Account

interface AccountsDao {

    /** returns id of created account **/
    fun createAccount(holder: String, initialBalance: Int = 0): Long
    fun findAll(): List<Account>
}