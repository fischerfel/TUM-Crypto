    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
    for(int i = 0; i < input.size(); i++){
        plainText = input.get(i);
        byte[] b = cipher.doFinal(plainText.getBytes("UTF-8"));
        String outString = new String(b);
        //to be written to file
