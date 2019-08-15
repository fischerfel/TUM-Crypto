        KeyStore ks = KeyStore.getInstance("PKCS11", p);
        ks.load(null, pin);

        X509Certificate cert1 = (X509Certificate) ks.getCertificate("rsa1");
        PublicKey pubk1 = cert1.getPublicKey();
        PrivateKey privk1 = (PrivateKey) ks.getKey("rsa1", pin);

        X509Certificate cert2 = (X509Certificate) ks.getCertificate("rsa2");
        PublicKey pubk2 = cert2.getPublicKey();
        PrivateKey privk2 = (PrivateKey) ks.getKey("rsa2", pin);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", p);
        cipher.init(Cipher.WRAP_MODE, pubk2); // here I get an exception
        byte[] output = cipher.doFinal(privk1.getEncoded());
