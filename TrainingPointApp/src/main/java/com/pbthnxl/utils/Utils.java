/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.utils;

/**
 *
 * @author DELL
 */
public class Utils {
    public static String getRating(int points) {
        if (points >= 90) {
            return "Xuất sắc";
        }
        if (points >= 80) {
            return "Tốt";
        }
        if (points >= 65) {
            return "Khá";
        }
        if (points >= 50) {
            return "Trung bình";
        }
        if (points >= 35) {
            return "Yếu";
        }
        return "Kém";
    }
}
