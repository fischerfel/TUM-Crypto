    KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount); 

    SecretKey key = SecretKeyFactory.getInstance( 
            "PBEWithSHA1And128BitAES-CBC-BC").generateSecret(keySpec);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");

    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));

    byte[] decrypted = cipher.doFinal(encrypted);
