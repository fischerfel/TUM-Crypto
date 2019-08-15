public String decrypt(String cipherText, byte[] encryptionKey) throws Exception {
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");          
    cipher.init(Cipher.DECRYPT_MODE, key, iV);
    String decrypt = new String(cipher.doFinal( Base64.decode(cipherText, Base64.DEFAULT)));

    byte[] decryptData = new byte[decrypt.getBytes().length - iV.getIV().length];
    System.arraycopy(decrypt.getBytes(), iV.getIV().length, decryptData, 0, decrypt.getBytes().length - iV.getIV().length);

    Log.d("decrypt = ", decrypt);

    decrypt = new String(decryptData, "UTF-8");

    return decrypt;
}   
