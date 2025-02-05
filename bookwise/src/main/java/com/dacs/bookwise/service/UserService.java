package com.dacs.bookwise.service;

import com.dacs.bookwise.dto.AuthResponse;
import com.dacs.bookwise.dto.SurveyResponseDTO;
import com.dacs.bookwise.dto.UserDTO;
import com.dacs.bookwise.entities.User;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    UserDTO getUserByUsername(String username);

    User findByEmail(String email);

    UserDTO getUserByEmail(String email);

    List<String> getWishlistByEmail(String email);

    void addBookToWishlist(String email, String bookId);

    void removeBookFromWishlist(String email, String bookId);

    void addBookToReadingHistory(String email, String bookId);

    List<String> getReadingHistoryByEmail(String email);


    AuthResponse validateAndLoginUser(String email, String rawPassword);

    String saveSurveyResponses(String email, List<SurveyResponseDTO> responses);

    String determinePreferredGenre(List<SurveyResponseDTO> surveyResponses);
}