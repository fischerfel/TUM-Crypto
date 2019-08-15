 String sKey ="fromdotnetpart";

 String sIv="fromdotnetiv";

    byte[] bKey = key.getBytes();
    byte[] iv = sIv.getBytes();
    SecretKeySpec skey = new SecretKeySpec(bKey, "AES");   
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    AlgorithmParameterSpec param = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, key,param);
    String decrypted = cipher.doFinal(encryptedString.getByte());
