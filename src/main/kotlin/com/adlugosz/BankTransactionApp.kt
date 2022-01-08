package com.adlugosz

import com.adlugosz.api.Accounts
import com.adlugosz.dao.DefaultAccountsDao
import com.adlugosz.infrastructure.DbConnFactory
import com.adlugosz.service.AccountsService
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.Jetty
import org.http4k.server.asServer
import kotlin.system.measureTimeMillis

val createAccount = """CREATE TABLE IF NOT EXISTS `Account` (
  `id`         INTEGER  PRIMARY KEY AUTO_INCREMENT,
  `holder` VARCHAR(50) NOT NULL,
  `balance`        INTEGER  NOT NULL,
  PRIMARY KEY(`id`)
);
""".trimIndent()

fun main() {
    measureTimeMillis {
        val dataSource = DbConnFactory.getDataSource()
        dataSource.connection.use {
            it.prepareStatement(createAccount).execute()
        }
        val defaultAccountsDao = DefaultAccountsDao(dataSource)
        val accountsService = AccountsService(defaultAccountsDao)
        val accountsRoute = Accounts(accountsService).accountsRoute()
        val printingApp: HttpHandler = PrintRequest().then(accountsRoute)
        accountsService.createAccount("test")

        val server = printingApp.asServer(Jetty(9000)).start()

        println("Server started on " + server.port())
    }.let { println("Start up took $it ms") }
}
