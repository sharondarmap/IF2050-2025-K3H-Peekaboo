package com.pekaboo.repositories;

import java.util.List;

import com.pekaboo.entities.User;

public interface UserRepository {
    void saveUser(User user);
    User getUserById(int id);
    User getUserByCredentials(String username, String password);
    void updateUser(User user);
    void deleteUser(int id);
    List<User> getAllUsers(); //hanya untuk admin kalau mau ngeliat semua user kalo butuh
}
