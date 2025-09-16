package com.todo.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class DatabaseConnection{
    public static final String URL="jdbc:mysql://localhost:3306/todo";
    public static final String USERNAME="root";
    public static final String PASSWORD=Dotenv.load().get("SQLpass");

    static {
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
    }
    catch(ClassNotFoundException e){
        System.out.println("JDBC driver is missing");
    }
    }
    public Connection getDBConnection() throws SQLException{
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

}