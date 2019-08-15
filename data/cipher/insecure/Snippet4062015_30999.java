public static byte[] encrypt(byte to_encrypt[], byte strkey[]) {
    try {           
        SecretKeySpec key = new SecretKeySpec(strkey, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);  
        return cipher.doFinal(to_encrypt); // <=========== error
    } catch (Exception e) { 
        e.printStackTrace();
        return null; 
    }
}
