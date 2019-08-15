public String crypto(SecretKey key, String inString, boolean decrypt){
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    byte[] inputByte = inString.getBytes("UTF-8");
    if (decrypt){
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String (cipher.doFinal(Base64.decode(inputByte, Base64.DEFAULT)));
    } else {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String (Base64.encode(cipher.doFinal(inputByte), Base64.DEFAULT));
    }
}
