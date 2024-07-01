/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author DELL
 */
public interface CloudinaryService {
    String uploadFile(MultipartFile file);
}
