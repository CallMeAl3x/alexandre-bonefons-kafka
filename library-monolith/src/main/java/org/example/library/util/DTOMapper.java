package org.example.library.util;

import org.example.library.dto.*;
import org.example.library.model.*;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public BookDTO toDto(Book b) {
        return BookDTO.builder()
                .id(b.getId()).title(b.getTitle()).author(b.getAuthor())
                .isbn(b.getIsbn()).genre(b.getGenre()).pages(b.getPages())
                .price(b.getPrice()).available(b.isAvailable()).build();
    }

    public Book toEntity(BookDTO d) {
        return Book.builder()
                .id(d.getId()).title(d.getTitle()).author(d.getAuthor())
                .isbn(d.getIsbn()).genre(d.getGenre()).pages(d.getPages())
                .price(d.getPrice()).available(d.isAvailable()).build();
    }

    public UserDTO toDto(User u) {
        return UserDTO.builder()
                .id(u.getId()).username(u.getUsername()).email(u.getEmail())
                .firstName(u.getFirstName()).lastName(u.getLastName())
                .membershipDate(u.getMembershipDate()).build();
    }

    public User toEntity(UserDTO d) {
        return User.builder()
                .id(d.getId()).username(d.getUsername()).email(d.getEmail())
                .firstName(d.getFirstName()).lastName(d.getLastName())
                .membershipDate(d.getMembershipDate()).build();
    }

    public LoanDTO toDto(Loan l) {
        return LoanDTO.builder()
                .id(l.getId())
                .bookId(l.getBook().getId()).bookTitle(l.getBook().getTitle())
                .userId(l.getUser().getId()).username(l.getUser().getUsername())
                .loanDate(l.getLoanDate()).returnDate(l.getReturnDate())
                .returned(l.isReturned()).fine(l.getFine()).build();
    }

    public ReviewDTO toDto(Review r) {
        return ReviewDTO.builder()
                .id(r.getId())
                .bookId(r.getBook().getId()).bookTitle(r.getBook().getTitle())
                .userId(r.getUser().getId()).username(r.getUser().getUsername())
                .rating(r.getRating()).comment(r.getComment())
                .createdAt(r.getCreatedAt()).build();
    }
}
