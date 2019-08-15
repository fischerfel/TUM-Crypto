public byte[] Encrypt(String plain) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        publicKey = "MyPublicKeyStringExtractedFromACertificate"

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());

        return encryptedBytes;
}
