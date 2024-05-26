/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.User;

/**
 *
 * @author DELL
 */
public interface UserRepository {
    User getUserByUsername(String username);
    int getIdByUsername(String username);
}
