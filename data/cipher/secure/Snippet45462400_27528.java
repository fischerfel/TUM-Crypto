public static String encrypt(String text, byte[] iv, byte[] key)throws Exception{ 

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    System.out.println("KEY SPECCCC: "+keySpec); 
    IvParameterSpec ivSpec = new IvParameterSpec(iv); 
    cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec); 
    byte [] results = cipher.doFinal(text.getBytes("UTF-8")); 
    BASE64Encoder encoder = new BASE64Encoder(); 
    return encoder.encode(results); 
} 
