package com.example.demo.todos.controller;

import com.example.demo.todos.features.create.command.CreateTodo;
import com.example.demo.todos.features.delete.command.DeleteTodo;
import com.example.demo.todos.features.findByUser.query.FindTodosByUser;
import com.example.demo.todos.features.findByUser.response.FindTodoByUserResponseDTO;
import com.example.demo.todos.features.updateStatus.command.UpdateStatusCommand;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;
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
    private final Poshtar poshtar;

    @GetMapping("user/{id}")
    public ResponseEntity<List<FindTodoByUserResponseDTO>> findTodosByUser(@PathVariable("id") Long userId) {
        var response = poshtar.send(new FindTodosByUser(userId));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(@RequestBody CreateTodo command) {
        poshtar.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{userId}/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long userId,
            @PathVariable Long todoId) {
        var command = new DeleteTodo(userId, todoId);
        poshtar.send(command);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    public ResponseEntity<Void> updateTodoStatus(@RequestBody UpdateStatusCommand command){
        poshtar.send(command);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
