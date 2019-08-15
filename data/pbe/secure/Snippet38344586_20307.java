 private static final String ALGO = "AES";

    /**
     * key value for encryption & decryption
     */
    private static String password = "sbifast12";
    private static byte[] iv = "QmBSbUZMUwld31DPrqyVSA==".getBytes();
    private static String IV = "QmBSbUZMUwld31DPrqyVSA==";
    private static String salt = "gettingsaltyfoo!";

    /**
     * constructor with two variable parameters
     * @param password
     * @param iv
     */
  /*  public AESEncryption(String password, String iv) {
        if (password == null || iv == null)
            throw new NullPointerException("Encryption values can not be null!");

        this.password = password.getBytes();
        this.iv = iv.getBytes();
    }*/

    /**
     * encrypt given string data
     *
     * @param rawdata
     * @return
     * @throws Exception
     */
    public static String encrypt(String rawdata) throws Exception {
        if (rawdata == null)
            throw new NullPointerException("Raw data can not be null!");
        //SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPassword(password, saltBytes);
        //SecretKey key = new SecretKeySpec(Base64.decode(salt,Base64.DEFAULT), "AES");
        // Key key = generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       /* cipher.init(Cipher.ENCRYPT_MODE, generateKeyFromPassword("sbifast12", salt.getBytes()),
                new IvParameterSpec(Arrays.copyOf(IV.getBytes("UTF-8"), 16)));
        */
        cipher.init(Cipher.ENCRYPT_MODE, generateKeyFromPassword("sbifast12", salt.getBytes()),
                new IvParameterSpec(Arrays.copyOf(IV.getBytes(),16)));
        byte[] encVal = cipher.doFinal(rawdata.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);

        System.out.println("%%%%%%% Encrypted Text: " + encryptedValue);
        return encryptedValue;
    }

    public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 1000, 128);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      //  SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithSHA256AND256BITAES");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * decrypt given string data
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedData) throws Exception {
        if (encryptedData == null)
            throw new NullPointerException("Encrypted data can not be null!");

        Key key = generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decodedValue = Base64.decode(encryptedData, Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    /**
     * key generator
     *
     * @return
     * @throws Exception
     */
    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec("sbifast12".getBytes(), ALGO);
        return key;
    }
