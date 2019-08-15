public void encrypt(){
    //encrypt
    EditText mEdit = (EditText)findViewById(R.id.editText);
    str_key = (String) mEdit.getText().toString();

    int iterationCount = 1000;
    int keyLength = 256;
    int saltLength = keyLength / 8; 

    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[saltLength];
    random.nextBytes(salt);
    KeySpec keySpec = new PBEKeySpec(str_key.toCharArray(), salt,
            iterationCount, keyLength);
    SecretKeyFactory keyFactory = null;
    try {
        keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    byte[] keyBytes = new byte[0];
    try {
        keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }
    SecretKey key = new SecretKeySpec(keyBytes, "Blowfish");

    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding"); 
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
    }
    if ( cipher == null || key == null) {
        //throw new Exception("Invalid key or cypher");
        str2="error";
    }
    else {
        byte[] iv = new byte[cipher.getBlockSize()];
        random.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key,ivParams);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }


        try {
            raw = cipher.doFinal(message.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        str2 = Base64.encodeToString(raw,Base64.DEFAULT);

    }


}
