package com.adlugosz.infrastructure

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DbConnFactory {
    private val configuration by lazy { HikariConfig() }
    private val dataSource: HikariDataSource by lazy { createConnection() }

    // todo move parameters to external property file
    private fun createConnection(
        connectionUrl: String = "jdbc:h2:~/test",
        user: String = "sa",
        password: String = "",
        additionalProps: Map<String, String>? = null
    ): HikariDataSource {
        configuration.jdbcUrl = connectionUrl
        configuration.username = user
        configuration.password = password
        additionalProps?.forEach { (k, v) -> configuration.addDataSourceProperty(k, v) }
        return HikariDataSource(configuration)
    }

    fun getDataSource(): DataSource = dataSource
}