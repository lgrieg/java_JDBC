package lgrieg;

import org.apache.commons.dbcp2.*;

import javax.sql.DataSource;

public class Driver {
    public static DataSource createDataSource(String url) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl(url);
        return dataSource;
    }
}
