/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.services.impl;

import com.pbthnxl.dto.AssistantDTO;
import com.pbthnxl.dto.StudentUserDTO;
import com.pbthnxl.pojo.User;
import com.pbthnxl.repositories.UserRepository;
import com.pbthnxl.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author DELL
 */
@Service("UserDetailsService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.userRepository.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
      
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(u.getUserRole()));
        
        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public int getIdByUsername(String username) {
        return this.userRepository.getIdByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        this.userRepository.saveUser(user);
    }

    @Override
    public void updateUserRole(int userId, String role) {
        this.userRepository.updateUserRole(userId, role);
    }

    @Override
    public boolean authUser(String username, String password) {
        return this.userRepository.authUser(username, password);
    }

    @Override
    public StudentUserDTO getUserByUsernameDTO(String username) {
        return this.convertToDTO(this.userRepository.getUserByUsername(username));
    }
    
    private StudentUserDTO convertToDTO(User u){
        StudentUserDTO dto = new StudentUserDTO();
        
        dto.setUserId(u.getId());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setEmail(u.getEmail());
        dto.setUsername(u.getUsername());
        dto.setAvatar(u.getAvatar());
        dto.setPhoneNumber(u.getPhoneNumber());
        dto.setStudentCode(u.getStudent().getStudentCode());
        dto.setClassName(u.getStudent().getClassId().getName());
        dto.setFacultyName(u.getStudent().getFacultyId().getName());
        dto.setRole(u.getUserRole());
        return dto;
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }
    
    private AssistantDTO convertToAssistantDTO(User assistant){
        AssistantDTO dto = new AssistantDTO();
        dto.setFirstName(assistant.getFirstName());
        dto.setLastName(assistant.getLastName());
        dto.setAvatar(assistant.getAvatar());
        dto.setEmail(assistant.getEmail());
        
        return dto;
    }

    @Override
    public List<AssistantDTO> getAssistantsDTO() {
        return this.userRepository.getAssistantList().stream().map(this::convertToAssistantDTO).collect(Collectors.toList());
    }
            
    
}
