/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.pojo.Otp;
import com.pbthnxl.pojo.User;
import com.pbthnxl.repositories.OtpRepository;
import com.pbthnxl.services.OtpService;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author hieu
 */
@PropertySource("classpath:mail.properties")
@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Environment env;
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;

    @Override
    public void generateOTP(User user, String email) {
        String OTP = RandomString.make(5);
        String encodedOTP = passwordEncoder.encode(OTP);
        Otp otp;

        if (user == null) {
            otp = this.otpRepo.findByEmail(email);
            if (otp != null) {
                otp.setOneTimePassword(encodedOTP);
                otp.setOtpRequestedTime(new Date());
            } else {
                otp = new Otp();
                otp.setEmail(email);
                otp.setOneTimePassword(encodedOTP);
                otp.setOtpRequestedTime(new Date());
            }
            
            otpRepo.save(otp);

        }

        try {
            sendOTPEmail(email, OTP);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OtpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(OtpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendOTPEmail(String email, String OTP) throws UnsupportedEncodingException, MessagingException {
        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
        String content = "<p>Hello " + email + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to register:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        try {
            this.sendEmail(email, subject, content);
        } catch (Exception ex) {
            Logger.getLogger(OtpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void clearOTP(Otp otp) {
        otp.setOneTimePassword(null);
        otp.setOtpRequestedTime(null);

        this.otpRepo.save(otp);
    }

    @Override
    public Otp findByEmail(String email) {
        return this.otpRepo.findByEmail(email);
    }

    public String sendEmail(String recipient, String subject, String body) throws Exception {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mail.port")));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        javaMailProperties.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        javaMailProperties.put("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
        javaMailProperties.put("mail.debug", env.getProperty("mail.debug"));

        mailSender.setJavaMailProperties(javaMailProperties);
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(recipient);
                message.setFrom(mailSender.getUsername());
                message.setSubject(subject);
                message.setBcc(mailSender.getUsername());
                message.setText(body, true);
            }
        };
        mailSender.send(preparator);

        return "Mail Sent Successfully.";
    }

    @Override
    public Boolean isOtpExpired(Otp otp) {
        long currentTimeInMillis = System.currentTimeMillis();
        long otpRequestedTimeInMillis = otp.getOtpRequestedTime().getTime();
        
        if(otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis)
            //OTP expires
            return false;
        
        return true;
    }

}
