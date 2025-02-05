package com.dacs.bookwise.controller;

import com.dacs.bookwise.dto.AuthResponse;
import com.dacs.bookwise.dto.QuestionDTO;
import com.dacs.bookwise.dto.SurveyResponseDTO;
import com.dacs.bookwise.dto.UserDTO;
import com.dacs.bookwise.service.UserService;
import com.dacs.bookwise.service.impl.QuestionServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("/book-wise/api")
public class BookWiseController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionServiceImpl questionService;

    @PostMapping(value = "/user/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated UserDTO userDTO) {
        UserDTO createdUser = userService.registerUser(userDTO);
        String token = "basic-token-" + createdUser.getId();
        AuthResponse response = new AuthResponse("Registro exitoso", token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserDTO> getUserInfo(@RequestParam String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }


    // Endpoint para actualizar un usuario
    @PutMapping(value = "/user/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String id,
            @RequestBody @Validated UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping(value = "/user/{username}", produces = "application/json")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/wishlist")
    public ResponseEntity<List<String>> getWishlist(@RequestParam String email) {
        List<String> wishlist = userService.getWishlistByEmail(email);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/user/wishlist")
    public ResponseEntity<Void> addToWishlist(@RequestParam String email, @RequestParam String bookId) {
        userService.addBookToWishlist(email, bookId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/user/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserDTO userDTO) {
        try {
            AuthResponse response = userService.validateAndLoginUser(userDTO.getEmail(), userDTO.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @DeleteMapping("/user/wishlist")
    public ResponseEntity<Void> removeFromWishlist(@RequestParam String email, @RequestParam String bookId) {
        userService.removeBookFromWishlist(email, bookId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/user/reading-history")
    public ResponseEntity<Void> addToReadingHistory(@RequestParam String email, @RequestParam String bookId) {
        userService.addBookToReadingHistory(email, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/reading-history")
    public ResponseEntity<List<String>> getReadingHistory(@RequestParam String email) {
        List<String> readingHistory = userService.getReadingHistoryByEmail(email);
        return ResponseEntity.ok(readingHistory);
    }

    @PostMapping("/user/survey")
    public ResponseEntity<String> saveSurveyResponses(@RequestParam String email, @RequestBody List<SurveyResponseDTO> responses) {
        String preferredGenre = userService.saveSurveyResponses(email, responses);
        return ResponseEntity.ok(preferredGenre);
    }

    @GetMapping("/questions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        System.out.println("Llamada al endpoint /questions");
        List<QuestionDTO> questions = questionService.getAllQuestions();
        System.out.println("Preguntas recuperadas: " + questions);
        return ResponseEntity.ok(questions);
    }

}

