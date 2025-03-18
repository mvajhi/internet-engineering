package org.example;

import java.util.Scanner;

import org.example.request.*;
import org.example.response.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.services.BookShopService;

public class ConsoleHandler {

    BookShopService bookShopService = new BookShopService();
    ObjectMapper mapper = new ObjectMapper();
    boolean isRunning = true;

    public void run(){
        Scanner console = new Scanner(System.in);

        while(isRunning){
            String line = console.nextLine();
            int commandIndex = line.indexOf(' ');
            if(commandIndex < 0){
                continue;
            }
            String command = line.substring(0,line.indexOf(' '));
            String json = line.substring(line.indexOf(' '));
            Response response = routeCommand(command,json);
            String output;
            try {
                output = mapper.writeValueAsString(response);
            } catch (JsonProcessingException e){
                output = "{\"success\": false, \"message\": \"error in json processing\"}";
            }
            System.out.println(output);
        }
        console.close();
    }
    private Response routeCommand(String command, String json){
        Response response = null;
        switch (command){
            case "add_user": response = addUserHandler(json);
                break;
            case "add_author": response = addAuthorHandler(json);
                break;
            case "add_book": response = addBookHandler(json);
                break;
            case "add_cart": response = addCart(json);
                break;
            case "add_credit": response = addCreditHandler(json);
                break;
            case "borrow_book": response = borrowBookHandler(json);
                break;
            case "add_review": response = addReview(json);
                break;
            case "show_user_details": response = addReview(json); // TODO: fix it
                break;
            case "show_author_details": response = showAuthorDetails(json);
                break;
            case "show_book_details": response = showBookDetails(json);
                break;
            case "exit": 
                isRunning = false;
                break;
            case "show_book_reviews": response = showBookReviews(json);
                break;
             case "show_purchased_books": response= showPurchasedBookHandler(json);
                 break;
             case "search_books_by_title": response= searchBookByTitleHandler(json);
                 break;
             case "search_books_by_author": response = searchBookByAuthorHandler(json);
                 break;
             case "search_books_by_genre": response = searchBookByGenreHandler(json);
                 break;
             case "search_books_by_year": response = searchBookByYearHandler(json);
                 break;

            default: response = new Response(false, "invalid command", null);
        }
        return response;
    }
    private Response addUserHandler(String json){
        try {
            AddUserRequest request = mapper.readValue(json, AddUserRequest.class);
            return bookShopService.addUser(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }

    }
    private Response addAuthorHandler(String json){
        try {
            AddAuthorRequest request = mapper.readValue(json, AddAuthorRequest.class);
            return bookShopService.addAuthor(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invaid json argument", null);
        }

    }
    private Response addBookHandler(String json){
        try{
            AddBookRequest request = mapper.readValue(json, AddBookRequest.class);
            return bookShopService.addBook(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }
    private Response addCart(String json){
        try{
            AddCartRequest request = mapper.readValue(json, AddCartRequest.class);
            return bookShopService.addShoppingCart(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }

    private Response addReview(String json){
        try {
            AddReviewRequest request = mapper.readValue(json, AddReviewRequest.class);
            return bookShopService.addReview(request);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }

    }

    private Response showUserDetails(String json){
        try {
            JsonNode jsonNode = mapper.readTree(json);
            String username = jsonNode.get("username").asText();
            return bookShopService.showUserDetails(username);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }

    private Response showAuthorDetails(String json){
        try {
            JsonNode jsonNode = mapper.readTree(json);
            String name = jsonNode.get("name").asText();
            return bookShopService.showAuthorDetails(name);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }

    private Response showBookDetails(String json){
        try {
            JsonNode jsonNode = mapper.readTree(json);
            String title = jsonNode.get("title").asText();
            return bookShopService.showBookDetails(title);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }

    private Response showBookReviews(String json){
        try {
            JsonNode jsonNode = mapper.readTree(json);
            String title = jsonNode.get("title").asText();
            return bookShopService.showBookReviews(title);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument",null);
        }
    }
    private Response addCreditHandler(String json){
        try {
            AddCreditRequest request = mapper.readValue(json, AddCreditRequest.class);
            return bookShopService.addCredit(request);
        }catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response borrowBookHandler(String json){
        try {
            BorrowBookRequest request = mapper.readValue(json, BorrowBookRequest.class);
            return bookShopService.borrowBook(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response showPurchasedBookHandler(String json){
        try {
            String request = mapper.readValue(json, String.class);
            return bookShopService.showPurchasedBooks(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response searchBookByTitleHandler(String json){
        try {
            String request = mapper.readValue(json, String.class);
            return bookShopService.searchBookByTitle(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response searchBookByYearHandler(String json){
        try {
            String request = mapper.readValue(json, String.class);
            return bookShopService.searchBookByTitle(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response searchBookByGenreHandler(String json){
        try {
            String request = mapper.readValue(json, String.class);
            return bookShopService.searchBookByTitle(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }
    private Response searchBookByAuthorHandler(String json){
        try {
            String request = mapper.readValue(json, String.class);
            return bookShopService.searchBookByTitle(request);
        } catch (JsonProcessingException e){
            return new Response(false, "invalid json argument", null);
        }
    }



}
