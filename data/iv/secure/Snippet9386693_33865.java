public byte[] encrypt(String cleartext) throws InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException,
        UnsupportedEncodingException, InvalidParameterSpecException {

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    byte[] encText = cipher.doFinal(cleartext.getBytes(CHARSET_NAME));
    byte[] iv = cipher.getParameters()
            .getParameterSpec(IvParameterSpec.class).getIV();

    byte[] enc = new byte[IV_SIZE + encText.length];

    for (int i = 0; i < enc.length; i++) {
        if (i < IV_SIZE)
            enc[i] = iv[i];
        else if (i < enc.length)
            enc[i] = encText[i - IV_SIZE];
    }

    return enc;
}

public String decrypt(byte[] encryptedText) throws InvalidKeyException,
        InvalidAlgorithmParameterException, UnsupportedEncodingException,
        IllegalBlockSizeException, BadPaddingException {

    byte[] iv = new byte[IV_SIZE];
    byte[] dec = new byte[encryptedText.length - IV_SIZE];

    for (int i = 0; i < encryptedText.length; i++) {
        if (i < IV_SIZE)
            iv[i] = encryptedText[i];
        else if (i < encryptedText.length)
            dec[i - IV_SIZE] = encryptedText[i];
    }

    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

    return new String(cipher.doFinal(dec), CHARSET_NAME);
}
