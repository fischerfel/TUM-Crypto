public String decryption(String inputData, String key, String certificate) {
    String decriptData = null;

    String verify = checkForCertificateConfig();
    if (!verify.equals("OK")) {
        return verify;
    }

    System.out.println("DECRYPTION INPUTDATA : " + inputData);
    System.out.println("DECRYPTION KEY : " + key);
    System.out.println("DECRYPTION CERTIFICATE : " + certificate);
    try {
        if (key.equalsIgnoreCase("Private")) {
            // System.out.println("DECRYPTION WITH PRIVATE KEY");
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(
                    certificate, null);
            decriptData = decryptString(inputData, privateKey);
        } else {
            // System.out.println("DECRYPTION WITH PUBLIC KEY");
            decriptData = decryptString(inputData,
                    keyStore.getCertificate(certificate).getPublicKey());
        }
    } catch (NoSuchPaddingException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (IllegalBlockSizeException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (NoSuchAlgorithmException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (UnrecoverableKeyException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (InvalidKeyException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (KeyStoreException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (BadPaddingException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (IOException ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    } catch (Exception ex) {
        decriptData = ex.getMessage();
        ex.printStackTrace();

    }
    return decriptData;
}   

private String decryptString(String dncStr, PrivateKey key)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IOException, IllegalBlockSizeException,
        BadPaddingException {
    String decStr = null;

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
     * Initialize Cipher Object with Private key and mode of Decryption
     */
    edCipher.init(Cipher.DECRYPT_MODE, key);
    /**
     * Decode the encrypted String convert into binary formate
     */
    byte[] encryptedDataStringBytes = this.bASE64Decoder
            .decodeBuffer(dncStr);
    /**
     * Decrypt the binary data and get Original encrypted String.
     */
    decStr = new String(edCipher.doFinal(encryptedDataStringBytes));

    return decStr;
}
