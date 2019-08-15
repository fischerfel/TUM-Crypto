public SecretKeyWrapper(Context context, String alias) throws GeneralSecurityException, IOException {
    mCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
    keyStore.load(null);

    if (!keyStore.containsAlias(alias)) {
        generateKeyPair(context, alias);
    }

    final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
    mPair = new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey());
}

private static void generateKeyPair(Context context, String alias) throws GeneralSecurityException {
    final Calendar start = new GregorianCalendar();
    final Calendar end = new GregorianCalendar();
    end.add(Calendar.YEAR, 100);

    final KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSubject(new X500Principal("CN=" + alias))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(start.getTime())
            .setEndDate(end.getTime())
            .build();

    final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
    gen.initialize(spec);
    gen.generateKeyPair();
}
