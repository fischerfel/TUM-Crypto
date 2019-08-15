public byte[] encrypt(byte[] msg, byte[] key, byte[] iv) throws Exception {
    //prepare key
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    //prepare cipher
    String cipherALG = "AES/CBC/PKCS5padding"; // use your preferred algorithm 
    Cipher cipher = Cipher.getInstance(cipherALG);
    String string = cipher.getAlgorithm();

    //as iv (Initial Vector) is only required for CBC mode
    if (string.contains("CBC")) {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);      
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    } else {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    }

    byte[] encMessage = cipher.doFinal(msg);        
    return encMessage;
}
