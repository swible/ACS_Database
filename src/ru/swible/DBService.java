package ru.swible;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

public class DBService {

    private Sql2o sql2o;

    public DBService() {
        sql2o = new Sql2o(configureDataBase());
    }

    public Sql2o getSql2o() {
        return sql2o;
    }

    private static DataSource configureDataBase() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://smpv.ru:3306/swible_acs?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8");
        hikariConfig.setUsername("acs");
        hikariConfig.setPassword("qwerty1");

        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setMaxLifetime(60_000);

        return new HikariDataSource(hikariConfig);
    }
}
