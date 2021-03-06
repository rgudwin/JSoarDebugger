/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Date;

/**
 *
 * @author rgudwin
 */
public class ToString {
    public static String from(Object n) {
        String s=null;
        if (n == null)
            s = "<NULL>";
        else if (n instanceof Long) {
            long i = (long) n;
            s = String.format("%d",i);
        }
        else if (n instanceof Integer) {
            int i = (int) n;
            s = String.format("%d",i);
        }
        else if (n instanceof Float) {
            float d = (float) n;
            s = String.format("%4.2f", d);
        }
        else if (n instanceof Double) {
            double d = (double) n;
            s = String.format("%4.2f", d);
        }
        else if (n instanceof Byte) {
            byte b = (byte) n;
            s = String.format("%x", b);
        }
        else if (n instanceof Boolean) {
            boolean b = (boolean) n;
            if (b == true) s = "true";
            else s = "false";
        }
        else if (n instanceof Date) {
            Date d = (Date) n;
            s = TimeStamp.getStringTimeStamp(d.getTime(),"dd/MM/yyyy HH:mm:ss.SSS");
        }
        else if (n instanceof String) {
            s = (String) n;
        }
        return(s);
    }
    
    public static String el(String name, int i) {
        String s = getSimpleName(name) +"["+i+"]";
        return(s);
    } 
    
    public static String getSimpleName(String fullname) {
        String[] mc = fullname.split("\\.");
        String simplename = mc[mc.length-1];
        return (simplename);
    }
    
}
