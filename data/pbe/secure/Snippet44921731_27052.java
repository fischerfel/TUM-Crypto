private String sign(String dataString, String pkString, String privateKeyPass) throws Exception {
        pkString = pkString.replace("-----BEGIN ENCRYPTED PRIVATE KEY-----", "");
        pkString = pkString.replace("-----END ENCRYPTED PRIVATE KEY-----", "");
        pkString = pkString.replaceAll("\\s+","");
        byte[] privateKeyBytes = decryptPrivateKey(Base64.decode(pkString, Base64.DEFAULT), privateKeyPass);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(privateKey);
        instance.update(dataString.getBytes(UTF_8));
        return Base64.encodeToString(instance.sign(), Base64.DEFAULT);
    }

public static byte[] decryptPrivateKey(byte[] key, String pass) throws Exception {
        PBEKeySpec passKeySpec = new PBEKeySpec(pass.toCharArray());

        EncryptedPrivateKeyInfo encryptedKey = new EncryptedPrivateKeyInfo(key);
        Timber.w("encryptedKey.getAlgName(): %s", encryptedKey.getAlgName());
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(encryptedKey.getAlgName());//PBES2
        SecretKey passKey = keyFac.generateSecret(passKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance(encryptedKey.getAlgName());
        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.DECRYPT_MODE, passKey, encryptedKey.getAlgParameters());

        // Decrypt the private key
        return pbeCipher.doFinal(encryptedKey.getEncryptedData());
    }
