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
//
//    @Test
//    fun `should fetch list of existing accounts and their balance`() {
//        //given
//        val account = Account()
//        val listOfAccounts = listOf(account.copy(), account.copy(), account.copy())
//        whenever(accountsDao.findAll()).thenReturn()
//        //when
//        val result = objectUnderTest.getAllAcounts()
//        //then
//        assertEquals(result, listOfAccounts)
//    }
}