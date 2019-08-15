private static String SK = "Secret Key in HEX";


//  To Encrupt

public static String encrypt( String Message ) throws Exception{

    byte[] KeyByte = hexStringToByteArray( SK);
    SecretKey k = new SecretKeySpec(KeyByte, 0, KeyByte.length, "DES");

    Cipher c = Cipher.getInstance("DES","SunJCE");
    c.init(1, k);
    byte mes_encrypted[] = cipher.doFinal(Message.getBytes());

    String MessageEncrypted = byteArrayToHexString(mes_encrypted);
    return MessageEncrypted;
}

//  To Decrypt

public static String decrypt( String MessageEncrypted )throws Exception{

    byte[] KeyByte = hexStringToByteArray( SK );
    SecretKey k = new SecretKeySpec(KeyByte, 0, KeyByte.length, "DES");

    Cipher dcr =  Cipher.getInstance("DES","SunJCE");
    dc.init(Cipher.DECRYPT_MODE, k);
    byte[] MesByte  = hexStringToByteArray( MessageEncrypted );
    byte mes_decrypted[] = dcipher.doFinal( MesByte );
    String MessageDecrypeted = new String(mes_decrypted);

    return MessageDecrypeted;
}

public static String byteArrayToHexString(byte bytes[]){

    StringBuffer hexDump = new StringBuffer();
    for(int i = 0; i < bytes.length; i++){
    if(bytes[i] < 0)
    {   
        hexDump.append(getDoubleHexValue(Integer.toHexString(256 - Math.abs(bytes[i]))).toUpperCase());
    }else
    {
        hexDump.append(getDoubleHexValue(Integer.toHexString(bytes[i])).toUpperCase());
    }
    return hexDump.toString();

}



public static byte[] hexStringToByteArray(String s) {

    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
    {   
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
    }
    return data;

} 
