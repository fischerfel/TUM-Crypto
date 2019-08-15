{

    byte[] enctext;
    byte[] dectext;
    String message="test";

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "SC");
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SC");
    keyGen.initialize(1024, random);
    KeyPair pair = keyGen.generateKeyPair();
    PrivateKey priv = pair.getPrivate();
    PublicKey pub = pair.getPublic();


    Cipher  rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding","SC");
    rsaCipher.init(Cipher.ENCRYPT_MODE, pub);
    enctext=rsaCipher.doFinal(message.getBytes());

    Cipher rsaCipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding","SC");
    rsaCipher2.init(Cipher.DECRYPT_MODE, priv);
    dectext=rsaCipher2.doFinal(enctext);
    sendSMS("5554",dectext.toString());// in debugger dectext shows equivalent "test" values in decimal but when passed as string to sendSMS func it shows some unknown format...

    }

    private void sendSMS(final String phoneNumber,String message)
    {
    }
