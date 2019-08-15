/* This MD5 hash class is compatible with the JavaScript MD5 has code found at http://pajhome.org.uk/crypt/md5/md5.html */
package com.DevFound;

import java.security.MessageDigest;

public class HexMD5 {
    public static String getMD5Str(String inputVal)throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputVal.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<byteData.length;i++) {
            String hex=Integer.toHexString(0xff & byteData[i]);
            if(hex.length()==1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
