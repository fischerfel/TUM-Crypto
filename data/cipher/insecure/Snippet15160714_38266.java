byte[] masterKey;
    if (Base64.decode(config.getProperty("encrMasterKey")) != null) {
        masterKey=aes.decrypt(Base64.decode(config.getProperty("encrMasterKey")),"password");
    } else {
        masterKey = aes.keyGeneration();
        byte[] encrMasterKey = aes.encrypt(masterKey, keyderivation("password"));
       writeToConfigFile("encrMasterKey", Base64.encode(encrMasterKey));
    }
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec keySpec = new SecretKeySpec(masterKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    byte[] cypherText = aes.encrypt(myJSONString,masterKey);'
