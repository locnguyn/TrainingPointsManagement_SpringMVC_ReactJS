/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services;

import com.pbthnxl.pojo.Otp;
import com.pbthnxl.pojo.User;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

/**
 *
 * @author hieu
 */
public interface OtpService {
    Otp findByEmail(String email);
    void generateOTP(User user, String email);
    void sendOTPEmail(String email, String OTP) throws UnsupportedEncodingException, MessagingException;
    void clearOTP(Otp otp);
    Boolean isOtpExpired(Otp otp);
}
