package com.todo;

import com.todo.util.DatabaseConnection;
import com.todo.gui.TodoAppGUI;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        try (Connection connection = dbConnection.getDBConnection()) {
            System.out.println("Successfully connected to the database!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());;
        }
        catch(ClassNotFoundException | InstantiationException |IllegalAccessException|UnsupportedLookAndFeelException e){
            System.err.println("Coulf not set look and feel"+e.getMessage());
        }
        SwingUtilities.invokeLater(()->{
        try
        {
            new TodoAppGUI().setVisible(true);
        }
        catch(Exception e){
            System.err.println("Error starting the application"+e.getLocalizedMessage());
        }

    }
        );

    }
}