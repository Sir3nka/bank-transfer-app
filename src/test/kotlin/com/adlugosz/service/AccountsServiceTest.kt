package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.adlugosz.dao.DefaultAccountsDao
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.lang.RuntimeException
import javax.sql.DataSource

internal class AccountsServiceTest {
    private val dataSource: DataSource = mock()
    private val accountsDao: AccountsDao = spy(DefaultAccountsDao(dataSource))
    private val objectUnderTest = AccountsService(accountsDao)
    private val holder = "holder"
    private val accountId = 1L

    @BeforeEach
    fun init() {
        whenever(dataSource.connection).thenReturn(mock())
    }

    @Test
    fun `should create new account successfully and return OK result`() {
        //given
        doReturn(accountId).whenever(accountsDao).createAccount(any(), any(), any())
        //when
        val result = objectUnderTest.createAccount(holder)
        //then
        verify(accountsDao).createAccount(any(), any(), any())
        assertEquals(Ok(accountId), result)
    }

    @Test
    fun `should fail to create new account and return failure when error is thrown from DAO object`() {
        //given
        val expectedException = RuntimeException()
        doThrow(expectedException).whenever(accountsDao).createAccount(any(), any(), any())
        //when
        val result = objectUnderTest.createAccount(holder)
        //then
        verify(accountsDao).createAccount(any(), any(), any())
        assertEquals(Err(expectedException), result)
    }

    @Test
    fun `should transfer funds from one account to another`() {
        //given
        val from = 1L
        val to = 2L
        val cash = 100
        //when
        doReturn(Unit).whenever(accountsDao).runInTransaction<Unit>(any())
        val result = objectUnderTest.transferFunds(from, to, cash)
        //then
        assertEquals(Ok(Unit), result)
    }

    @Test
    fun `should not transfer funds from one account to another where not enough funds`() {
        //given
        val from = 1L
        val to = 2L
        val cash = 100
        val expectedException = RuntimeException()
        doThrow(expectedException).whenever(accountsDao).runInTransaction<Unit>(any())

        //when
        val result = objectUnderTest.transferFunds(from, to, cash)
        //then
        assertEquals(Err(expectedException), result)
    }
}