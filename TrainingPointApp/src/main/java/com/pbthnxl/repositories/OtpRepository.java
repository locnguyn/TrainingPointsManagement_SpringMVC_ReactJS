/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.repositories;

import com.pbthnxl.pojo.Otp;

/**
 *
 * @author hieu
 */
public interface OtpRepository {
    Otp findByEmail(String email);
    void save(Otp otp);
}
