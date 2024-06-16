/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.controllers;

import com.pbthnxl.pojo.Otp;
import com.pbthnxl.pojo.User;
import com.pbthnxl.services.OtpService;
import com.pbthnxl.services.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<String> sendEmailOtp(@RequestBody Map<String, String> params) {

        User user = this.userService.getUserByEmail(params.get("email"));

        if (user != null) {
            return new ResponseEntity<>("Email Exists", HttpStatus.BAD_REQUEST);
        }

        if (params.containsKey("email")) {
            this.otpService.generateOTP(user, params.get("email"));
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);

    }

    @CrossOrigin
    @GetMapping(path = "/verify/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        Otp otp = this.otpService.findByEmail(params.get("email"));
        if (otp == null) {
            response.put("message", "Otp not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (params.containsKey("otp")) {
            String OTP = params.get("otp");
            if (this.otpService.isOtpExpired(otp)) {
                response.put("message", "Otp expired");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else if (this.passwordEncoder.matches(OTP, otp.getOneTimePassword())) {
                response.put("message", "Verified");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Otp not match");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }

        response.put("message", "Failed");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
