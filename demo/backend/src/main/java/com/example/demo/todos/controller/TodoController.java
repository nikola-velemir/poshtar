package com.example.demo.todos.controller;

import com.example.demo.todos.features.create.command.CreateTodo;
import com.example.demo.todos.features.getByUser.query.FindTodosByUser;
import com.example.demo.todos.features.getByUser.response.FindTodoByUserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.example.core.mediator.IMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/todos")
public class TodoController {
    @Autowired
    private final IMediator mediator;

    @GetMapping("user/{id}")
    public ResponseEntity<List<FindTodoByUserResponseDTO>> findTodosByUser(@PathVariable("id") Long userId) {
        var response = mediator.send(new FindTodosByUser(userId));
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<Void> createTodo(@RequestBody CreateTodo command){
        mediator.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
