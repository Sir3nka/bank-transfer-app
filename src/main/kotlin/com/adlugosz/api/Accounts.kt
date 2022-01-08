package com.adlugosz.api

import com.adlugosz.domain.Account
import com.adlugosz.service.AccountsService
import com.github.michaelbull.result.fold
import org.http4k.core.Body
import org.http4k.core.Method.*
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

class Accounts(private val accountsService: AccountsService) {
    private val accountLens = Body.auto<CreateAccountRequest>().toLens()
    private val outgoingAccountLens = Body.auto<List<Account>>().toLens()
    private val transferLens = Body.auto<TransferRequest>().toLens()

    fun accountsRoute() = "/accounts" bind routes(
        "/" bind routes ( POST to { request: Request ->
            accountLens(request).let {
                accountsService.createAccount(it.holder, it.initialBalance).fold(
                    success = { Response(Status.CREATED).body(it.toString()) },
                    failure = { Response(Status.BAD_REQUEST) }
                )
            }
        }, GET to { outgoingAccountLens.inject(accountsService.getAllAccounts(), Response(Status.OK)) }),
        "/{from}/transfer/{to}" bind routes( PUT to { request ->
            transferLens(request).let {
                accountsService.transferFunds(
                    from = request.path("from")!!.toLong(),
                    to = request.path("to")!!.toLong(),
                    cash = it.cash
                )
            }.fold(
                success = { Response(Status.OK) },
                failure = { Response(Status.INTERNAL_SERVER_ERROR) }
            )
        } ))
}

// for simplicity no floating types
data class CreateAccountRequest(val holder: String, val initialBalance: Int)
data class TransferRequest(val cash: Int)