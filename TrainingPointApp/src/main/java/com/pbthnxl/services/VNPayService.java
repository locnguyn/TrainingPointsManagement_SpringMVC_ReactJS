/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pbthnxl.services;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author DELL
 */
public interface VNPayService {
    String createOrder(int total, String orderInfor, String urlReturn);
    int orderReturn(HttpServletRequest request);
}
