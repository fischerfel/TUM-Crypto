private static byte[] encrypt(byte[] raw, byte[] clear) throws 
   Exception {  
    SecretKeySpec skeySpec = new SecretKeySpec(raw,  "AES");  
    Cipher cipher = null;

    if(isIVUsedForCrypto) {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(IV));  
    }
    else 
    {
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
    }
    byte[] encrypted = cipher.doFinal(clear);  
    return encrypted;  
}  

 public static byte[] toByte(String hexString) { 

    int len = hexString.length()/2;  
    byte[] result = new byte[len];  
    try{
    for (int i = 0; i < len; i++) { 
        result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2),16).byteValue();  
    }
    }catch (Exception e) {

    }
    return result;  
}  

public static String toHex(byte[] buf) {  
    if (buf == null)  
        return "";  
    StringBuffer result = new StringBuffer(2*buf.length);  
    for (int i = 0; i < buf.length; i++) {  
        appendHex(result, buf[i]);  
    }  
    return result.toString();  
}  
private final static String HEX = "0123456789ABCDEF";  
private static void appendHex(StringBuffer sb, byte b) {  
    sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));  
}  
