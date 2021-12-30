package com.adlugosz.api

import com.adlugosz.service.AccountsService
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import org.http4k.core.Method.POST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CREATED
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class AccountTest {
    private val objectMapper = ObjectMapper()
    private val accountsService: AccountsService = mock()
    private val objectUnderTest = Accounts(accountsService)

    @Test
    fun `create new account`() {
        //given
        val requestBody = CreateAccountRequest(holder = "Holder", 5)
        //when
        whenever(accountsService.createAccount(any(), any())).thenReturn(Ok(0))
        //then
        assertEquals(objectUnderTest.accountsRoute()(Request(POST, "/accounts").body(objectMapper.writeValueAsString(requestBody))), Response(CREATED) )
    }

    @Test
    fun `return bad request on failed request`() {
        //given
        val requestBody = CreateAccountRequest(holder = "Holder", 5)
        //when
        whenever(accountsService.createAccount(any(), any())).thenReturn(Err(mock()))
        //then
        assertEquals(objectUnderTest.accountsRoute()(Request(POST, "/accounts").body(objectMapper.writeValueAsString(requestBody))), Response(BAD_REQUEST) )
    }
}