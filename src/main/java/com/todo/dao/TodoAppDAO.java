package com.todo.dao;
import com.todo.model.Todo;
import com.todo.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TodoAppDAO {
    private static final String SELECT_ALL_TODOS="select * from todos order by created_at DESC";
    private static final String INSERT_TODO="insert into todos(title,description,completed,created_at,updated_at)values(?,?,?,?,?)";

    public int createtodo(Todo todo){
        try(
            Connection conn=DatabaseConnection.getDBConnection();
        )
    }
    private Todo getTodoRow(ResultSet rs) throws SQLException{
        int id =rs.getInt("id");
        String title=rs.getString("title");
        String description=rs.getString("description");
        Boolean completed=rs.getBoolean("completed");
        LocalDateTime createdAt=rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt=rs.getTimestamp("updated_at").toLocalDateTime();
        Todo todo=new Todo(id,title,description,completed,createdAt,updatedAt);
        return todo;
    }
    public List<Todo> getAllTodos() throws SQLException {
        List<Todo> todos = new ArrayList<>();
    
        
        try (Connection conn = DatabaseConnection.getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_TODOS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Todo todo = new Todo();
                todo.setId(rs.getInt("id"));
                todo.setTitle(rs.getString("title"));
                todo.setDescription(rs.getString("description"));
                todo.setCompleted(rs.getBoolean("completed"));
                todo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                
                if (rs.getTimestamp("updated_at") != null) {
                    todo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                
                todos.add(todo);
            }
            

        }
        return todos;

    }
}
