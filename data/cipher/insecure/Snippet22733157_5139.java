/** Utility method to Encrpyt a plain text string using blowfish algorithm. This method is synchronised between threads.
     * Base64 encoding is used to encode and decode byte array.
     * <p>NOTE: use the same key for Encryption and Decryption</p>
     * 
     * @param plainText Plain text String
     * @param key       Secret key ( If null default will be used)
     * @return String   URL safe encrypted String
     * @throws Exception
     */

    public synchronized static String blowfishEncryption(String plainText, String key) throws Exception {
        if(DEBUG) {
            logger.log(Level.INFO,"blowfishEncryption() method ===== passed normal text: { "+plainText+" passed key: "+key+" }");
        }
        if(key==null) {
            logger.log(Level.INFO,"passed key is null using default key");
            key=BLOWFISH_SECRET;
        }
        ByteArrayOutputStream os= new ByteArrayOutputStream(1024);
        byte[] keyByte = hexToBytes(key);
        SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "Blowfish");
        Cipher ecipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] stringByte=plainText.getBytes("US-ASCII");

        byte[] econtent=ecipher.doFinal(stringByte);

        String out= new String(Base64.encodeBase64(econtent), "US-ASCII");

        return out;
    }

    /** Utility method for Blowfish Decryption. This method is synchronised between threads. 
     * <p>NOTE: use the same key for Encryption and Decryption</p>
     * 
     * @param cipherContent     Cipher Text Byte array to be decrypted
     * @param key               Key used for Decryption. NOTE: use same key for encryption and decryption
     * @return String           Plain text String
     * @throws Exception
     */

    public synchronized static String blowfishDecryption(String cipherText, String key) throws Exception {
        // String ciphertext is base 64 endoded string This method returns plain text string

        if(DEBUG) {
            logger.log(Level.INFO,"blowfishEncryption() method ===== passed key: "+key+" }");
        }
        if(key==null) {
            logger.log(Level.INFO,"passed key is null using default key");
            key=BLOWFISH_SECRET;
        }

        byte[] myKeyByte = hexToBytes(key);

        SecretKeySpec skeySpec = new SecretKeySpec(myKeyByte, "Blowfish");

        Cipher ecipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");

        ecipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] cipherContent=cipherText.getBytes("US-ASCII");


        byte[]  dContent=ecipher.doFinal(cipherContent);

        String out=new String(Base64.encodeBase64(dContent), "US-ASCII");

        return out;
    }
