/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pbthnxl.utils;


/**
 *
 * @author hieu
 */
public enum PaypalIntent {
    // capture
    CAPTURE("CAPTURE"),
    //authorize
    AUTHORIZE("AUTHORIZE");
    
    private final String value;
    
    PaypalIntent(String value){
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    
}
