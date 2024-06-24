package com.pbthnxl.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Helpers {
    private static int transIdDefault = 1;

     public static String getAppTransId() {
        if (transIdDefault >= 100000) {
            transIdDefault = 1;
        }

        transIdDefault += 1;
        SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMdd_hhmmss");
        String timeString = formatDateTime.format(new Date());
        return String.format("%s%06d", timeString, transIdDefault);
    }

    public static String getMac(String key, String data) throws NoSuchAlgorithmException, InvalidKeyException {
        return Objects.requireNonNull(HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key, data));
     }
}