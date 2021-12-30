package com.adlugosz.api

import com.adlugosz.service.AccountsService
import com.github.michaelbull.result.fold
import org.http4k.core.Body
import org.http4k.core.Method.POST
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind


class Accounts(private val accountsService: AccountsService) {
    private val accountLens = Body.auto<CreateAccountRequest>().toLens()

    fun accountsRoute() = "/accounts" bind POST to { request ->
        accountLens(request).let {
            accountsService.createAccount(it.holder, it.initialBalance).fold(
                success = { Response(Status.CREATED) },
                failure = { Response(Status.BAD_REQUEST) }
            )
        }
    }
}

// for simplicity no floating types
data class CreateAccountRequest(val holder: String, val initialBalance: Int)