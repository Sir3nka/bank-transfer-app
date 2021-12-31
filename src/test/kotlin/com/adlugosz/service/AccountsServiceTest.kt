package com.adlugosz.service

import com.adlugosz.dao.AccountsDao
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.RuntimeException

internal class AccountsServiceTest {
    private val accountsDao: AccountsDao = mock()

    private val objectUnderTest = AccountsService(accountsDao)

    private val holder = "holder"

    private val accountId = 1L

    @Test
    fun `should create new account successfully and return OK result`() {
        //given
        whenever(accountsDao.createAccount(any(), any())).thenReturn(accountId)
        //when
        val result = objectUnderTest.createAccount(holder)
        //then
        verify(accountsDao).createAccount(any(), any())
        assertEquals(Ok(accountId), result)
    }

    @Test
    fun `should fail to create new account and return failure when error is thrown from DAO object`() {
        //given
        val expectedException = RuntimeException()
        whenever(accountsDao.createAccount(any(), any())).thenThrow(expectedException)
        //when
        val result = objectUnderTest.createAccount(holder)
        //then
        verify(accountsDao).createAccount(any(), any())
        assertEquals(Err(expectedException), result)
    }

    @Test
    fun `should transfer funds from one account to another`() {
        //given
        val from = 1L
        val to = 2L
        val cash = 100
        //when
        val result = objectUnderTest.transferFunds(from, to)
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
        //when
        val result = objectUnderTest.transferFunds(from, to)
        //then
        assertEquals(Err(expectedException), result)
    }
}