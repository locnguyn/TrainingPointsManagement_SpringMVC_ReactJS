/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.utils;

/**
 *
 * @author hieu
 */
public enum PaypalPaySource {
    card("card"),
    paypal("paypal");

    private String value;
     
    PaypalPaySource (String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
   
   
    
}
