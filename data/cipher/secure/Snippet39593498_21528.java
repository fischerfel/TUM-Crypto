static String IV = "AAAAAAAAAAAAAAAA";

static String encryptionKey = "0123456789ABCDEF";

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding"/*, "SunJCE"*/);
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
    }
    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding"/*, "SunJCE"*/);
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }
//To Encrypt

byte[] cipher = encrypt(plaintext, encryptionKey);

System.out.print("cipher: ");

//To Decrypt

String decrypted = decrypt(cipher, encryptionKey);

System.out.println("decrypt: " + decrypted);
