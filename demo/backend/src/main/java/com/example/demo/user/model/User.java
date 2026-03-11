package com.example.demo.user.model;

import com.example.demo.todos.model.TodoItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "username", nullable = false)
    String username;
    @Column(name = "passowrd", nullable = false)
    String password;
    @Column(name = "first_name", nullable = false)
    String firstName;
    @Column(name = "last_name", nullable = false)
    String lastName;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoItem> todos = new ArrayList<>();

    public User(String username, String hashedPassword, String firstName, String lastName) {
        this.id = null;
        this.username = username;
        this.password = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.todos = new ArrayList<>();
    }

    public void addTodo(TodoItem todo) {
        todos.add(todo);
    }

    public void removeTodo(TodoItem todo) {
        todos.remove(todo);
    }
}