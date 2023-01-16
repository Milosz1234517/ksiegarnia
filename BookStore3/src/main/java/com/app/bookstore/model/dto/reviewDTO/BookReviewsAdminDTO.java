package com.app.bookstore.model.dto.reviewDTO;

import com.app.bookstore.model.dto.userDTO.UserDTO;
import com.app.bookstore.model.dto.bookDTO.BookHeaderTitleDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewsAdminDTO {

    private Integer reviewId;

    private String description;

    private Integer mark;

    private UserDTO user;

    private BookHeaderTitleDTO bookHeader;

}
