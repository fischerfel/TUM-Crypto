 String text16 = "6F4B1B252A5F0C3F2992E1A65E56E5B8";
    String secret16 = "c4dcc3c6ce0acaec4327b6098260b0be";

    SecretKeySpec sks = new SecretKeySpec(secret16.getBytes(),"AES");
    Cipher c = Cipher.getInstance("AES");

    c.init(Cipher.ENCRYPT_MODE, sks);
    c.update(text16.getBytes());
    byte[] ciphertext = c.doFinal();
    Log.d("resultdebug",new String(Hex.encode(ciphertext), "ASCII"));
