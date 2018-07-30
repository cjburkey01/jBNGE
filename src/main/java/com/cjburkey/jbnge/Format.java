package com.cjburkey.jbnge;

public final class Format {
    
    public static String format(double num, int places) {
        return String.format("%." + places + "f", num);
    }
    
    public static String format1(double num) {
        return format(num, 1);
    }
    
    public static String format2(double num) {
        return format(num, 2);
    }
    
    public static String format3(double num) {
        return format(num, 3);
    }
    
    public static String format4(double num) {
        return format(num, 4);
    }
    
}