public byte[] encryptedAes(char[] input) throws Exception {
        // WRONG
        // aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
        //
        aesCipher.init(Cipher.ENCRYPT_MODE, aesSecretKey, 
                       new IvParameterSpec(aesInitialisationVector);
        CharBuffer cBuf = CharBuffer.wrap(input);
        byte[] normalised = Charset.forName("UTF-8").encode(cBuf).array();
        byte[] ciphertext = aesCipher.doFinal(normalised);
        return ciphertext;
}
