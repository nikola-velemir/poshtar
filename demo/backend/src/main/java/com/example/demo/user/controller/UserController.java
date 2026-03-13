package com.example.demo.user.controller;

import com.example.demo.user.features.activate.command.ActivateUserCommand;
import com.example.demo.user.features.login.query.LoginQuery;
import com.example.demo.user.features.login.response.LoginResponseDTO;
import com.example.demo.user.features.register.command.RegisterCommand;
import com.example.demo.user.features.getById.query.GetUserByIdQuery;
import com.example.demo.user.features.getById.response.GetUserResponseDTO;
import org.example.core.mediator.IMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private final IMediator mediator;


    public UserController(IMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("{id}")
    public ResponseEntity<GetUserResponseDTO> findUser(@PathVariable Long id) {
        return ResponseEntity.ok(mediator.send(new GetUserByIdQuery(id)));
    }
    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginQuery query){
       LoginResponseDTO response = mediator.send(query);
       return ResponseEntity.ok(response);
    }
    @PostMapping("register")
    public ResponseEntity<Void> createUser(@RequestBody RegisterCommand command) {
        mediator.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("activate/{name}")
    public ResponseEntity<Void> activateUser(@PathVariable("name") String username){
        mediator.send(new ActivateUserCommand(username));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
