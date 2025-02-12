package com.example.FinalWorkDevelopmentOnSpringFramework.init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ExecuteSqlScript {


    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private DataSource dataSource;

    public void executeSqlFile(String fileName) throws SQLException {
        Connection connection = dataSource.getConnection();
        ScriptUtils.executeSqlScript(connection, resourceLoader.getResource("classpath:" + fileName));
        connection.close();
    }
    }








