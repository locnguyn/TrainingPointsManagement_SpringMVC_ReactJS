/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.User;
import java.util.List;

/**
 *
 * @author DELL
 */
public interface UserRepository {
    User getUserByUsername(String username);
    User getUserByEmail(String email);
    int getIdByUsername(String username);
    void saveUser(User user);
    void updateUserRole(int userId, String role);
    boolean authUser(String username, String password);
    List<User> getAssistantList();
}
