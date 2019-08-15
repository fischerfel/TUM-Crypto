protected static byte[] getHashedKey(String password,String MODE)throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    saltIV = new byte[48];
    salt = new byte[32];
    ivBytes =new byte[16];

    if(MODE.equals("CREATE_VAULT")){
        //generate salt ,iv & save them
        salt = generateSalt();
        ivBytes = generateIV();

        System.arraycopy(salt, 0, saltIV, 0, salt.length);
        System.arraycopy(ivBytes, 0, saltIV, salt.length, ivBytes.length);

        //save salt & iv
        //FileOutputStream saltIvOutFile = new FileOutputStream("C:\\saltIv.ats");
        //saltIvOutFile.write(saltIV);
        //saltIvOutFile.close();
    }
    if(MODE.equals("OPEN_VAULT")){
        FileInputStream saltIvInFile = new FileInputStream("C:\\saltIv.ats");
        saltIvInFile.read(saltIV);
        saltIvInFile.close();

        System.arraycopy(saltIV, 0, salt, 0, salt.length);
        System.arraycopy(saltIV, salt.length, ivBytes, 0, ivBytes.length);
    }

    // Derive the key
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            pswdIterations,
            keySize
    );

    SecretKey secretKey = factory.generateSecret(spec);

    return secretKey.getEncoded();
}
