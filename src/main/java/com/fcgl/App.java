/*
 * Starter.
 */
package com.fcgl;

import com.sun.prism.impl.Disposer;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//import org.jooq.generated.tables.ItemConditionRecord;
//import static test.generated.Tables.*;

//import static test.generated.Tables.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import static org.jooq.generated.Tables.*;
import org.jooq.SQLDialect;


import java.sql.*;


//import java.sql.Connection;
//import java.sql.DriverManager;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
