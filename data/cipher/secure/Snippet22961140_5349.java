public void keyEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    eKey = cipher.doFinal(key.getBlowfishKeyBytes());           //symmetric key encrypted with public key
    //System.out.println("2. cipherText= " + bytesToHex(symmKey));
}
