package com.dacs.bookwise.repository;

import com.dacs.bookwise.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByEmail(String email); // Método adicional para buscar por correo
    boolean existsByUsername(String username); // Verifica si existe un usuario por nombre
    boolean existsByEmail(String email); // Verifica si existe un usuario por correo
}
