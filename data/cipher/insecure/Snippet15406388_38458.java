    SecureRandom sr = new SecureRandom();  
    DESKeySpec dks = new DESKeySpec(rawKeyData);

    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
    SecretKey key = keyFactory.generateSecret(dks);  
    Cipher cipher = Cipher.getInstance("DES");  
    cipher.init(Cipher.ENCRYPT_MODE, key, sr);  
    // why the sr is necessary to init a Cipher object?
    byte data[] = str.getBytes();  
    byte[] encryptedData = cipher.doFinal(data);
