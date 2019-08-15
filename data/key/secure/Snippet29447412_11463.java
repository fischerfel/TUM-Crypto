public String decrypt(String cipherText, byte[] encryptionKey) throws Exception {
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");          
    cipher.init(Cipher.DECRYPT_MODE, key, iV);
    String decrypt = new String(cipher.doFinal( Base64.decode(cipherText, Base64.DEFAULT)));
    decrypt = new String(Arrays.copyOfRange(decrypt.getBytes(), 16, decrypt.getBytes().length));

    return decrypt;
} 
