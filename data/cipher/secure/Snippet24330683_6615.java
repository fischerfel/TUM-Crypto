@Test
public void chunkDecrypt() throws Exception {
    key = MessageDigest.getInstance("MD5").digest("som3C0o7p@s5".getBytes());
    iv = Hex.decode("EECE34808EF2A9ACE8DF72C9C475D751");
    byte[] ciphertext = Hex
            .decode("EF26839493BDA6DA6ABADD575262713171F825F2F477FDBB53029BEADB41928EA5FB46737D7A94D5BE74B6049008443664F0E0D883943D0EFBEA09DB");

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

    byte[] fullDecryptedPlainText = cipher.doFinal(ciphertext);
    assertThat(new String(fullDecryptedPlainText),
            is("The quick brown fox jumps over the lazy dogs"));

    byte[] first32 = Arrays.copyOfRange(ciphertext, 0, 32);
    byte[] final28 = Arrays.copyOfRange(ciphertext, 32, 60);
    byte[] decryptedChunk = new byte[32];

    int num = cipher.update(first32, 0, 32, decryptedChunk);
    assertThat(num, is(16));
    assertThat(new String(decryptedChunk, 0, 16), is("The quick brown "));

    num = cipher.update(first32, 0, 32, decryptedChunk);
    assertThat(num, is(32));
    assertThat(new String(decryptedChunk, 0, 16), is("fox jumps over t"));

    num = cipher.update(final28, 0, 24, decryptedChunk);
    assertThat(num, is(44));
    assertThat(new String(decryptedChunk, 0, 12), is("he lazy dogs"));
}
