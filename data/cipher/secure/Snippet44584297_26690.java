    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.ENCRYPT_MODE, pk);

    DigestInfo di = new DigestInfo(new DefaultDigestAlgorithmIdentifierFinder().find("SHA-256"), hash);
    byte[] digestInfo = di.getEncoded();
    byte[] data = c.doFinal(digestInfo);
