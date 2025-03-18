package org.example.controller;

import org.example.request.AddUserRequest;
import org.example.response.Response;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Response addUser(@RequestBody AddUserRequest addUserRequest) {
        return userService.addUser(addUserRequest);
    }
}