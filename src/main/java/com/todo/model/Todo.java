package main.java.com.todo.model;

import java.util.LocalDateTime;

public class Todo{
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Todo(){
        this.created_at=LocalDateTime.now();
        this.updated_at=LocalDateTime.now();
    }

    public Todo(String title,String description){
        this();
        this.title=title;
        this.description=description;

    }

    public Todo(int id,String title,String description,boolean completed, LocalDateTime created_at,LocalDateTime updated_at){
        this.id=id;
        this.title=title;
        this.description=description;
        this.completed=completed;
        this.created_at=created_at;
        this.updated_at=updated_at
    }

}