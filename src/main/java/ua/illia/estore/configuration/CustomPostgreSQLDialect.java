package ua.illia.estore.configuration;

import org.hibernate.dialect.PostgreSQL10Dialect;

public class CustomPostgreSQLDialect extends PostgreSQL10Dialect {

    public CustomPostgreSQLDialect() {
        registerFunction("fts", new PostgreSQLFTSFunction());
        registerFunction("count_over", new PostgreSQLOverFunction());
    }
}