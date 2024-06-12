/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.dto.StudentUserDTO;
import com.pbthnxl.pojo.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author DELL
 */
public interface UserService extends UserDetailsService {
    User getUserByUsername(String username);
    int getIdByUsername(String username);
    public void saveUser(User user);
    void updateUserRole(int userId, String role);
    boolean authUser(String username, String password);
    StudentUserDTO getUserByUsernameDTO(String username);
}
