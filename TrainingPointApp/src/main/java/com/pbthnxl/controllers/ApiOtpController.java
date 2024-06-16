/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Otp;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.OtpService;
import com.pbthnxl.services.UserService;
import java.util.Map;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.javamail.MimeMessagePreparator;
/**
 *
 * @author hieu
 */
@RestController
@RequestMapping("/api/otp")
public class ApiOtpController {

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @CrossOrigin
    @PostMapping("/create/")
    public ResponseEntity<String> sendEmailOtp(@RequestParam Map<String, String> params) {

        User user = this.userService.getUserByEmail(params.get("email"));

        if (user != null) {
            return new ResponseEntity<>("Email Exists", HttpStatus.BAD_REQUEST);
        }

        this.otpService.generateOTP(user, params.get("email"));

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/verify/")
    public ResponseEntity<String> verifyOtp(@RequestParam Map<String, String> params) {
        Otp otp = this.otpService.findByEmail(params.get("email"));
        if (otp == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        String OTP = "";
        if (params.containsKey("otp")) {
            OTP = params.get("otp");
            if (this.otpService.isOtpExpired(otp)) {
                //OTP not expire
                if (this.passwordEncoder.matches(OTP, otp.getOneTimePassword())) {
                    return new ResponseEntity<>("Verified", HttpStatus.OK);
                } else return new ResponseEntity<>("Otp not match", HttpStatus.OK); 
            } else return new ResponseEntity<>("Otp expires", HttpStatus.OK); 

        }

        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

}
