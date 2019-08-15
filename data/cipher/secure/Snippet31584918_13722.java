    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");

    byte[] keyBytes =   Base64.decode(this.publicKey, 0);

    /* the strToPublicKey is the previews code block */
    PublicKey publickey = strToPublicKey(new String(keyBytes));
    cipher.init( Cipher.ENCRYPT_MODE , publickey );

    // Base 64 encode removed.
    //byte[] encryptedBytes = Base64.encode( cipher.doFinal(plainText.getBytes()), 0 );
    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
