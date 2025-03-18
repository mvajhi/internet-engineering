package org.example.controller;

import org.example.entities.User;
import org.example.request.AddUserRequest;
import org.example.request.LoginRequest;
import org.example.response.Response;
import org.example.services.UserService;
import org.example.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request) {
        User user = userService.findUser(request.getUsername());
        if (user == null || AuthenticationUtils.loggedIn()) {
            return Response.fail();
        }
        AuthenticationUtils.login(user);
        return Response.successful();
    }
    @PostMapping("/add")
    public Response addUser(@RequestBody AddUserRequest addUserRequest) {
        return userService.addUser(addUserRequest);
    }
}