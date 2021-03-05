package com.ssh.shuoshi.util;

public class DoubleUtil {

    public static Double getDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (Exception exception) {
            exception.printStackTrace();
            return 0.0;
        }
    }
}
