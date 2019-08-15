for (int j = 0; j < pocetIteraci; j++) {
    String text = randomString(46);

    long time = System.currentTimeMillis();
    sifruj(padding, provider, generateVectot(),text);

    time = System.currentTimeMillis() - time;
    System.out.println("Time: " + time + " ms");
}

public static byte[] sifruj(String padding, Provider provider, IvParameterSpec finalniInicializacniVektor,String text)
        throws Exception {
    return zpracuj(padding, provider, text.getBytes("UTF-8"), finalniInicializacniVektor, Cipher.ENCRYPT_MODE);
}

public static byte[] zpracuj(String padding, Provider provider, byte[] data,
        IvParameterSpec finalniInicializacniVektor, int mode) throws Exception {
    final SecretKeySpec klicSpec = new SecretKeySpec(klic, ALGORITHM_AES);
    final Cipher sifra = Cipher.getInstance(padding, provider);
    sifra.init(mode, klicSpec, finalniInicializacniVektor);

    return sifra.doFinal(data);

}
