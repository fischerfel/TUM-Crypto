@Test
public void chunkDecrypt() throws Exception {
    byte[] key = MessageDigest.getInstance("MD5").digest("som3C0o7p@s5".getBytes());
    byte[] iv = Hex.decode("EECE34808EF2A9ACE8DF72C9C475D751");
    byte[] ciphertext = Hex
            .decode("EF26839493BDA6DA6ABADD575262713171F825F2F477FDBB53029BEADB41928EA5FB46737D7A94D5BE74B6049008443664F0E0D883943D0EFBEA09DB");

    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

    int chunkSize = 16;
    byte[] inBuffer = new byte[chunkSize];
    int outBufferSize = ((chunkSize + 15) / 16) * 16;
    byte[] outBuffer = new byte[outBufferSize];

    for (int i = 0; i < ciphertext.length; i += chunkSize) {
        int thisChunkSize = Math.min(chunkSize, ciphertext.length - i);
        System.arraycopy(ciphertext, i, inBuffer, 0, thisChunkSize);
        int num = cipher.update(inBuffer, 0, thisChunkSize, outBuffer);
        if (num > 0) {
            logger.debug("update #" + ((i / chunkSize) + 1) + " - data <"
                    + new String(outBuffer, 0, num) + ">");
        }
    }
    int num = cipher.doFinal(inBuffer, chunkSize, 0, outBuffer);
    logger.debug("doFinal - data <" + new String(outBuffer, 0, num) + ">");
}
