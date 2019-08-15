   public static String Encrypt(String plain, SharedPreferences.Editor editor)        
   throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,    
   IllegalBlockSizeException, BadPaddingException
    {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();


        privateKey = kp.getPrivate();
        Gson gson4 = new Gson();
        String json4 = gson4.toJson(privateKey);
        editor.putString("privateKey", json4);

        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        encrypted = bytesToString(encryptedBytes);


        return encrypted;

    }
