package org.example.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

public class Response {
    boolean success;
    String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timeStamp = LocalDateTime.now();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Object data;

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Response successful() {
        return new Response(true, "successful", null);
    }

    public static Response fail() {
        return new Response(false, "Bad request", null);
    }

    public boolean isSuccess() {
        return success;
    }

    public Response setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Response setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp=timeStamp;
        return this;
    }

    public String convertToString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e){
            return "{\"success\": false, \"message\": \"error in json processing\"}";
        }
    }
}
