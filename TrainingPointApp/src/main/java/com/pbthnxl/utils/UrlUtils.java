package com.pbthnxl.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UrlUtils {
    public String getBaseUrl(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }
}
