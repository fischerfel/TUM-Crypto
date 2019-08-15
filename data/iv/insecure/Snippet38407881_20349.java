    DESedeKeySpec keyspec
            = new DESedeKeySpec(key.getBytes());
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(EncryptionAlgorithm.DESede.name());
    SecretKey deskey = keyfactory.generateSecret(keyspec);

    // Create an 8-byte initialization vector
    byte[] iv = new byte[]{(byte)  0x39,  0x39, 0x39, (byte)  0x39,  0x39,  0x39,  0x39,  0x39};

    IvParameterSpec ivVector = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance(EncryptionAlgorithm.DESede.name() + "/" + EncryptionMode.CBC.name() + "/"
            + EncryptionPadding.PKCS5Padding.name());
    byte[] ciphertext = null;
    try {
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ivVector);
        ciphertext = cipher.doFinal(pass.getBytes());
    } catch (Exception e) {

    }
