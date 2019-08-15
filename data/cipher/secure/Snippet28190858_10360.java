private byte[] decryptSecretKeyData(byte[] encryptedSecretKey, byte[] iv, PrivateKey privateKey) throws Exception 
{
    try {

        Provider provider= new sun.security.pkcs11.SunPKCS11(keyStoreFile1);
        Security.addProvider(provider);

        LOG.info("**************Inside decryptSecretKeyData***********************");
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING", provider);

        // decrypting the session key with rsa no padding.
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey); 

        /* The reason is RSA OAEP SHA256 is not supported in HSM. */
        byte[] decKey = rsaCipher.doFinal(encryptedSecretKey);

        OAEPEncoding encode = new OAEPEncoding(new RSAEngine(), new SHA256Digest(), iv);
        LOG.info("******************RSAPublicKey rsaPublickey = (*****************************");

        java.security.interfaces.RSAPublicKey rsaPublickey = (java.security.interfaces.RSAPublicKey) publicKeyFile;
        RSAKeyParameters keyParams = new RSAKeyParameters(false, rsaPublickey.getModulus(), EXPONENT);
        encode.init(false, keyParams);

        LOG.info("******************encode.processBlock(decKey, 0, decKey.length);************************");
        byte decryptedSecKey[] = encode.processBlock(decKey, 0, decKey.length);

        return decryptedSecKey;
    } catch (InvalidCipherTextException e) {
        LOG.info("*******************Failed to decrypt AES secret key using RSA :**********************");
        throw new Exception("Failed to decrypt AES secret key using RSA :" + e.toString());
    }

}
