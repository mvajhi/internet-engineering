package org.example.controller;

import org.example.entities.User;
import org.example.request.AddUserRequest;
import org.example.request.LoginRequest;
import org.example.response.AuthResponse;
import org.example.response.Response;
import org.example.services.SessionService;
import org.example.services.UserService;
import org.example.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userService.findUser(request.getUsername());
        if (user == null || AuthenticationUtils.loggedIn()) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, "Login failed", null, null));
        }
        
        if(Objects.equals(request.getPassword(), user.getPassword())) {
            AuthenticationUtils.login(user);
            
            // Generate session token and store in Redis
            String token = sessionService.createSession(user.getUsername());
            
            // Create response with user data and token
            AuthResponse response = new AuthResponse(true, "Login successful", token, user.getUsername());
            
            // Return token in both body and Authorization header
            return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(response);
        } else {
            return ResponseEntity.badRequest()
                .body(new AuthResponse(false, "Password incorrect", null, null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (!AuthenticationUtils.loggedIn()) {
            return ResponseEntity.badRequest().body(Response.fail());
        }
        
        // Get current user from AuthenticationUtils
        String username = AuthenticationUtils.getUsername();
        
        // Invalidate the session in Redis
        sessionService.invalidateSession(username);
        
        // Logout from AuthenticationUtils
        AuthenticationUtils.logout();
        
        return ResponseEntity.ok(Response.successful());
    }
    
    @GetMapping("/check-session")
    public ResponseEntity<Response> checkSession() {
        if (AuthenticationUtils.loggedIn()) {
            return ResponseEntity.ok(new Response(true, "Session is valid", null));
        } else {
            return ResponseEntity.status(401).body(new Response(false, "Session is invalid or expired", null));
        }
    }

    @PostMapping("/add")
    public Response addUser(@RequestBody AddUserRequest addUserRequest) {
        return userService.addUser(addUserRequest);
    }
}