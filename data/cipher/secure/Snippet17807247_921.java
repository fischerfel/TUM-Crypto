public String RSADecrypt(byte[] encryptedBytes) throws NoSuchAlgorithmException, NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException {

    privateKey = getPrivateKey();
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] cipherData = cipher.doFinal(encryptedBytes);
    return new String(cipherData,"UTF-16BE");               
    }

public String RSAEncrypt(String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException {

    publicKey = getPublicKey();
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] cipherData = cipher.doFinal(plain.getBytes());           
    return new String(cipherData,"UTF-16BE");
    }
