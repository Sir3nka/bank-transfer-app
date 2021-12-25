package com.adlugosz.api

import com.adlugosz.app
import com.fasterxml.jackson.databind.ObjectMapper
import org.http4k.core.Method.POST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED

internal class AccountTest {

    private val objectMapper = ObjectMapper()

    @Test
    fun `create new account`() {
        val requestBody = CreateAccountRequest(holder = "Holder", 5)
        assertEquals(app(Request(POST, "/accounts").body(objectMapper.writeValueAsString(requestBody))), Response(CREATED) )
    }
}