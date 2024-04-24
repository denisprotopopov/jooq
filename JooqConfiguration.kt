package ru.raiffeisen.rise.core.configuration

import org.jooq.ExecuteContext
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListener
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.jdbc.support.SQLExceptionTranslator
import javax.sql.DataSource


@Configuration
class JooqConfiguration(val dataSource: DataSource) {

    @Bean
    fun connectionProvider(): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
    }

    @Bean
    public fun dsl(): DefaultDSLContext {
        return DefaultDSLContext(configuration());
    }

    public fun configuration(): DefaultConfiguration {
        val jooqConfiguration = DefaultConfiguration();
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(SQLDialect.POSTGRES)
        jooqConfiguration
            .set( DefaultExecuteListenerProvider(ExceptionTranslator()));

        return jooqConfiguration;
    }
}


internal class ExceptionTranslator : DefaultExecuteListener() {
    override fun exception(context: ExecuteContext) {
        val dialect = context.configuration().dialect()
        val translator: SQLExceptionTranslator =
            SQLErrorCodeSQLExceptionTranslator(dialect.thirdParty().springDbName()!!)
        context.exception(translator.translate("Access database using jOOQ", context.sql(), context.sqlException()!!))
    }
}
