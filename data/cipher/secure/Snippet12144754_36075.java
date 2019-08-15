    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    PrivateKey privateKey = null;
    PublicKey publicKey = null;

    // Load certificate from keystore
    try {
        FileInputStream keystoreFileInputStream = new FileInputStream("keystore.jks");
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(keystoreFileInputStream, "passphrase".toCharArray());

        try {
            privateKey = (PrivateKey) keystore.getKey("idm_key", "passphrase".toCharArray());

        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } catch (Exception e) {
        // TODO broad exception block
        e.printStackTrace();
    }

    // Make the encrypted data.

byte[] toEncrypt = "Data to encrypt".getBytes();
    byte[] encryptedData = null;

    // Perform private key encryption 
    try {
        // Encrypt the data
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        encryptedData = cipher.doFinal(toEncrypt);

    } catch (Exception e) {
        // TODO broad exception block
        e.printStackTrace();
    }
