package org.example.controller;

import org.example.entities.BookShop;
import org.example.entities.Role;
import org.example.entities.User;
import org.example.response.AuthResponse;
import org.example.services.SessionService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class OAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private BookShop bookShop;

    private final String clientId = "YOUR_GOOGLE_CLIENT_ID"; //TODO Replace with your actual client ID
    private final String clientSecret = "YOUR_GOOGLE_CLIENT_SECRET"; //TODO Replace with your actual client secret

    @PostMapping("/google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody GoogleAuthRequest request) {
        try {
            GoogleTokenResponse tokenResponse = verifyGoogleToken(request.getCredential());
            if (!tokenResponse.getAud().equals(clientId)) {
                throw new SecurityException("Invalid token audience");
            }

            GoogleUserInfo userInfo = extractUserInfo(tokenResponse);
            User user = userService.findUserByEmail(userInfo.getEmail());
            if (user == null) {
                user = new User(
                        userInfo.getEmail(), "", userInfo.getEmail(), Role.CUSTOMER, null
                );
                bookShop.addUser(user);
            }

            String jwt = sessionService.createSession(user.getUsername());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(new AuthResponse(
                            true,
                            "Google login successful",
                            jwt,
                            user.getUsername()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Google authentication failed",
                            null, null));
        }
    }

    private GoogleTokenResponse verifyGoogleToken(String credential) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id_token", credential);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return restTemplate.postForObject(
                "https://oauth2.googleapis.com/tokeninfo",
                request,
                GoogleTokenResponse.class
        );
    }

    private GoogleUserInfo extractUserInfo(GoogleTokenResponse tokenResponse) {
        GoogleUserInfo userInfo = new GoogleUserInfo();
        userInfo.setEmail(tokenResponse.getEmail());
        userInfo.setName(tokenResponse.getName());
        userInfo.setPicture(tokenResponse.getPicture());
        return userInfo;
    }

    public static class GoogleAuthRequest {
        private String credential;

        public String getCredential() {
            return credential;
        }

        public void setCredential(String credential) {
            this.credential = credential;
        }
    }

    public static class GoogleTokenResponse {
        private String email;
        private String name;
        private String picture;
        private String iss;
        private String aud;
        private Long exp;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }

        public String getAud() {
            return aud;
        }

        public void setAud(String aud) {
            this.aud = aud;
        }

        public Long getExp() {
            return exp;
        }

        public void setExp(Long exp) {
            this.exp = exp;
        }
    }

    public static class GoogleUserInfo {
        private String email;
        private String name;
        private String picture;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}