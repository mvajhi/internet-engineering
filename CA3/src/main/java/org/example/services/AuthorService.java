package org.example.services;

import org.example.request.AddAuthorRequest;
import org.example.entities.Author;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AuthorService {

    public Author createAuthor(AddAuthorRequest request) {
        Author newAuthor = new Author();
        newAuthor.setBorn(convertStringToDate(request.getBorn()));
        newAuthor.setName(request.getName());
        newAuthor.setPenName(request.getPenName());
        newAuthor.setNationality(request.getNationality());
        if(request.getDied() != null)
            newAuthor.setDied(convertStringToDate(request.getDied()));
        return newAuthor;
    }
    private LocalDate convertStringToDate(String date){
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date,formatter);
    }
}
