public String encryption(String inputData, String key, String certificate) // Certificate is nothing but aliase name
{
    String encriptData = null;

    String verify = checkForCertificateConfig();
    if (!verify.equals("OK")) {
        return verify;
    }
    System.out.println("ENCRYPTION INPUTDATA : " + inputData);
    System.out.println("ENCRYPTION KEY : " + key);
    System.out.println("ENCRYPTION CERTIFICATE : " + certificate);
    try {
        if (key.equalsIgnoreCase("Private")) {
            // System.out.println("ENCRYPTION WITH PRIVATE KEY");
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    certificate, null);
            encriptData = encryptString(inputData, privateKey);
        } else {
            // System.out.println("ENCRYPTION WITH PUBLIC KEY");
            encriptData = encryptString(inputData,
                    keyStore.getCertificate(certificate).getPublicKey());
        }
    } catch (NoSuchPaddingException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (IllegalBlockSizeException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (NoSuchAlgorithmException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (UnrecoverableKeyException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (InvalidKeyException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (KeyStoreException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (BadPaddingException ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (Exception ex) {
        encriptData = ex.getMessage();
        ex.printStackTrace();

    }
    return encriptData;
}

private String encryptString(String encStr, PrivateKey key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String encoutStr = null;

    /**
     * first check key generation algorithm and initialize Cipher object
     * according algorithm
     */
    if (key.getAlgorithm().equalsIgnoreCase("RSA")) {
        edCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    } else if (key.getAlgorithm().equalsIgnoreCase("DSA")) {
        edCipher = Cipher.getInstance("DSA/ECB/PKCS1Padding");
    }

    /**
     * Initialize Cipher Object with Private key and mode of Encryption
     */
    edCipher.init(Cipher.ENCRYPT_MODE, key);
    byte[] buff = encStr.getBytes();
    /**
     * Encrypt the String and get binary data
     */
    byte[] encryptedDataStringBytes = edCipher.doFinal(buff);
    /**
     * Encode the binary data into String formate
     */
    encoutStr = this.bASE64Encoder.encode(encryptedDataStringBytes);

    return encoutStr;
}
