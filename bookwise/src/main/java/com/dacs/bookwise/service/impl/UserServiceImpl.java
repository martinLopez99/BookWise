package com.dacs.bookwise.service.impl;

import com.dacs.bookwise.dto.*;
import com.dacs.bookwise.entities.*;
import com.dacs.bookwise.mapper.UserMapper;
import com.dacs.bookwise.repository.QuestionRepository;
import com.dacs.bookwise.repository.UserRepository;
import com.dacs.bookwise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = mapUserDTOToUser(userDTO);
        initializeDefaults(user);
        User savedUser = userRepository.save(user);
        return mapUserToUserDTO(savedUser);
    }

    // Actualizar un usuario
    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Actualiza los campos manualmente
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getPreferences() != null) {
            existingUser.setPreferences(userDTO.getPreferences());
        }
        // Otros campos según sea necesario...

        User updatedUser = userRepository.save(existingUser);
        return mapUserToUserDTO(updatedUser);
    }


    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.userToUserDTO(user);
    }

    @Override
    public List<String> getWishlistByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getWishlistBookIds();
    }

    @Override
    public void addBookToWishlist(String email, String bookId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getWishlistBookIds().contains(bookId)) {
            user.getWishlistBookIds().add(bookId);
            userRepository.save(user);
        }
    }

    @Override
    public void removeBookFromWishlist(String email, String bookId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.getWishlistBookIds().remove(bookId);
        userRepository.save(user);
    }

    @Override
    public void addBookToReadingHistory(String email, String bookId) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getReadingHistory().stream().noneMatch(entry -> entry.getGoogleBookId().equals(bookId))) {
            ReadingEntry entry = new ReadingEntry();
            entry.setGoogleBookId(bookId);
            user.getReadingHistory().add(entry);
            userRepository.save(user);
        }
    }

    @Override
    public List<String> getReadingHistoryByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getReadingHistory().stream()
                .map(ReadingEntry::getGoogleBookId)
                .collect(Collectors.toList());
    }

    @Override
    public AuthResponse validateAndLoginUser(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        String token = "dummy-token-12345";
        return new AuthResponse("Login exitoso", token);
    }


    @Override
    public String saveSurveyResponses(String email, List<SurveyResponseDTO> responsesDTOList) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        List<SurveyResponse> existingSurveys = user.getSurveyResponses();

        if (existingSurveys == null) {
            existingSurveys = new ArrayList<>();
        }

        for (SurveyResponseDTO dto : responsesDTOList) {
            // Buscar si ya existe una encuesta con el mismo surveyId
            SurveyResponse existingSurvey = existingSurveys.stream()
                    .filter(survey -> survey.getSurveyId().equals(dto.getSurveyId()))
                    .findFirst()
                    .orElse(null);

            if (existingSurvey != null) {
                // Actualizar encuesta existente
                existingSurvey.setResponses(dto.getResponses().stream()
                        .map(this::mapAnswerDTOToAnswer)
                        .collect(Collectors.toList()));
            } else {
                // Crear una nueva encuesta con un surveyId generado dinámicamente
                SurveyResponse newSurvey = new SurveyResponse();
                newSurvey.setSurveyId(UUID.randomUUID().toString());
                newSurvey.setResponses(dto.getResponses().stream()
                        .map(this::mapAnswerDTOToAnswer)
                        .collect(Collectors.toList()));
                existingSurveys.add(newSurvey);
            }
        }

        // Guardar las encuestas actualizadas en el usuario
        user.setSurveyResponses(existingSurveys);
        user.setPreferences(determinePreferredGenre(responsesDTOList));
        userRepository.save(user);

        return user.getPreferences();
    }

    @Override
    public String determinePreferredGenre(List<SurveyResponseDTO> surveyResponses) {
        Map<String, Integer> genreScores = new HashMap<>();

        for (SurveyResponseDTO survey : surveyResponses) {
            for (AnswerDTO answer : survey.getResponses()) {
                String genre = questionGenreMap.get(answer.getQuestionId());
                if (genre != null) {
                    int score = answer.getSelectedOption().getAnswer() * answer.getSelectedOption().getWeight();
                    genreScores.put(genre, genreScores.getOrDefault(genre, 0) + score);
                }
            }
        }

        return genreScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin preferencia");
    }

    private Question findQuestionById(String questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    private SurveyResponse mapSurveyResponseDTOToSurveyResponse(SurveyResponseDTO dto) {
        SurveyResponse response = new SurveyResponse();
        response.setSurveyId(dto.getSurveyId());
        response.setResponses(dto.getResponses().stream().map(this::mapAnswerDTOToAnswer).toList());
        return response;
    }

    private Answer mapAnswerDTOToAnswer(AnswerDTO dto) {
        Answer answer = new Answer();
        answer.setQuestionId(dto.getQuestionId());
        Option option = new Option();
        option.setAnswer(dto.getSelectedOption().getAnswer());
        option.setWeight(dto.getSelectedOption().getWeight());
        answer.setSelectedOption(option);
        return answer;
    }


    // Método manual para mapear de UserDTO a User
    private User mapUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPreferences(userDTO.getPreferences() != null ?
                userDTO.getPreferences():
                "");
        user.setWishlistBookIds(userDTO.getWishlistBookIds() != null ?
                userDTO.getWishlistBookIds() :
                new ArrayList<>());
        user.setReadingHistory(userDTO.getReadingHistory() != null ?
                mapReadingHistoryDTOToReadingHistory(userDTO.getReadingHistory()) :
                new ArrayList<>());
        user.setSurveyResponses(userDTO.getSurveyResponses() != null ?
                mapSurveyResponsesDTOToSurveyResponses(userDTO.getSurveyResponses()) :
                new ArrayList<>());
        return user;
    }


    // Método manual para mapear de User a UserDTO
    private UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPreferences(user.getPreferences());
        userDTO.setWishlistBookIds(user.getWishlistBookIds());
        userDTO.setReadingHistory(mapReadingHistoryToReadingHistoryDTO(user.getReadingHistory()));
        userDTO.setSurveyResponses(mapSurveyResponsesToSurveyResponsesDTO(user.getSurveyResponses()));
        return userDTO;
    }

    private List<ReadingEntry> mapReadingHistoryDTOToReadingHistory(List<ReadingEntryDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream().map(dto -> {
            ReadingEntry entry = new ReadingEntry();
            entry.setGoogleBookId(dto.getGoogleBookId());
            entry.setRating(dto.getRating());
            entry.setNotes(dto.getNotes());
            return entry;
        }).toList();
    }

    private List<ReadingEntryDTO> mapReadingHistoryToReadingHistoryDTO(List<ReadingEntry> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(entity -> {
            ReadingEntryDTO dto = new ReadingEntryDTO();
            dto.setGoogleBookId(entity.getGoogleBookId());
            dto.setRating(entity.getRating());
            dto.setNotes(entity.getNotes());
            return dto;
        }).toList();
    }

    private List<SurveyResponse> mapSurveyResponsesDTOToSurveyResponses(List<SurveyResponseDTO> dtoList) {
        if (dtoList == null) return null;
        return dtoList.stream().map(dto -> {
            SurveyResponse response = new SurveyResponse();
            response.setSurveyId(dto.getSurveyId());
            response.setResponses(dto.getResponses().stream().map(answerDTO -> {
                Answer answer = new Answer();
                answer.setQuestionId(answerDTO.getQuestionId());
                Option option = new Option();
                option.setAnswer(answerDTO.getSelectedOption().getAnswer());
                option.setWeight(answerDTO.getSelectedOption().getWeight());
                answer.setSelectedOption(option);
                return answer;
            }).toList());
            return response;
        }).toList();
    }

    private List<SurveyResponseDTO> mapSurveyResponsesToSurveyResponsesDTO(List<SurveyResponse> entityList) {
        if (entityList == null) return null;
        return entityList.stream().map(entity -> {
            SurveyResponseDTO dto = new SurveyResponseDTO();
            dto.setSurveyId(entity.getSurveyId());
            dto.setResponses(entity.getResponses().stream().map(answer -> {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setQuestionId(answer.getQuestionId());
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setAnswer(answer.getSelectedOption().getAnswer());
                optionDTO.setWeight(answer.getSelectedOption().getWeight());
                answerDTO.setSelectedOption(optionDTO);
                return answerDTO;
            }).toList());
            return dto;
        }).toList();
    }

    private void initializeDefaults(User user) {
        if (user.getWishlistBookIds() == null) {
            user.setWishlistBookIds(new ArrayList<>());
        }
        if (user.getReadingHistory() == null) {
            user.setReadingHistory(new ArrayList<>());
        }
        if (user.getSurveyResponses() == null) {
            user.setSurveyResponses(new ArrayList<>());
        }
    }

    private static final Map<String, String> questionGenreMap = Map.ofEntries(
            Map.entry("675f6ae6382d0623cccf7e01", "Fantasy"),
            Map.entry("675f6ae6382d0623cccf7e02", "Drama"),
            Map.entry("675f6ae6382d0623cccf7e03", "Thriller"),
            Map.entry("675f6ae6382d0623cccf7e04", "Science Fiction"),
            Map.entry("675f6ae6382d0623cccf7e05", "Adventure"),
            Map.entry("675f6ae6382d0623cccf7e06", "Ethics"),
            Map.entry("675f6ae6382d0623cccf7e07", "Fantasy"),
            Map.entry("675f6ae6382d0623cccf7e08", "Fantasy"),
            Map.entry("675f6ae6382d0623cccf7e09", "Thriller"),
            Map.entry("675f6ae6382d0623cccf7e10", "Mystery"),
            Map.entry("675f6ae6382d0623cccf7e11", "Mystery"),
            Map.entry("675f6ae6382d0623cccf7e12", "Mystery"),
            Map.entry("675f6ae6382d0623cccf7e13", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e14", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e15", "Romance"),
            Map.entry("675f6ae6382d0623cccf7e16", "Historical"),
            Map.entry("675f6ae6382d0623cccf7e17", "Historical"),
            Map.entry("675f6ae6382d0623cccf7e18", "Historical"),
            Map.entry("675f6ae6382d0623cccf7e19", "Horror"),
            Map.entry("675f6ae6382d0623cccf7e20", "Horror")
    );

}
