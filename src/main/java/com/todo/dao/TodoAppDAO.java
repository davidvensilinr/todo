package com.todo.dao;
import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.todo.model.Todo;
import com.todo.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.sql.Statement;


public class TodoAppDAO {
    private static final String SELECT_ALL_TODOS="select * from todos order by created_at DESC";
    private static final String INSERT_TODO="insert into todos(title,description,completed,created_at,updated_at)values(?,?,?,?,?)";
    private static final String SELECT_TODO_BY_ID="select * from todos where id=?";
    private static final String UPDATE_TODO="update todos set title=?,description=?,completed=?,updated_at=? where id=?";
    public int createtodo(Todo todo) throws SQLException{
        try(
            Connection conn=DatabaseConnection.getDBConnection();
            PreparedStatement stmt=conn.prepareStatement(INSERT_TODO,Statement.RETURN_GENERATED_KEYS);
        ){
            stmt.setString(1,todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3,todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(todo.getCreatedAt()));
            stmt.setTimestamp(5,Timestamp.valueOf(todo.getUpdatedAt()));

            int rowAffected=stmt.executeUpdate();
            if(rowAffected==0){
                throw new SQLException("Creating todo failed, no row is inserted");

            }
            try(ResultSet generatedKeys=stmt.getGeneratedKeys()){
                if(generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
                else{
                    throw new SQLException("Creating todo failed, no id obtained");
                }
            }



        }
    }
    public Todo getTodoById(int id) throws SQLException {
        try(
            Connection conn = DatabaseConnection.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_TODO_BY_ID);
        ){
            stmt.setInt(1, id);
            ResultSet res= stmt.executeQuery();
            if(res.next()){
                return getTodoRow(res);
            }
            
        }
        return null;
    }
    public boolean updateTodo(Todo todo) throws SQLException{
        try(
            Connection conn=DatabaseConnection.getDBConnection();
            PreparedStatement stmt=conn.prepareStatement(UPDATE_TODO);

        )
        {
            stmt.setString(1, todo.getTitle());
            stmt.setString(2,todo.getDescription());
            stmt.setBoolean(3,todo.isCompleted());
            stmt.setTimestamp(4,Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(5,todo.getId());

            int rowAffected=stmt.executeUpdate();
            return rowAffected>0;



        }

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
