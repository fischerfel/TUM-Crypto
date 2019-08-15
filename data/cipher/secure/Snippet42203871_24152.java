    byte[] randNum1 = hexStringToByteArray(randNum1_s);
    byte[] message = hexStringToByteArray(message_s);
    byte[] iv = hexStringToByteArray(iv_s);

    // Deriving encryption and mac keys.
    HKDFBytesGenerator hkdfBytesGenerator = new HKDFBytesGenerator(new SHA256Digest());
    byte[] khdfInput = ByteUtils.concatenate(randNum1, preMasterKey);
    hkdfBytesGenerator.init(new HKDFParameters(khdfInput, null, "Android".getBytes(Charset.forName("UTF-8"))));
    byte[] encryptionKey = new byte[16];
    hkdfBytesGenerator.generateBytes(encryptionKey, 0, 16);
    byte[] macKey = new byte[16];
    hkdfBytesGenerator.generateBytes(macKey, 0, 16);

    // Verifying Message Authentication Code (aka mac/tag)
    Mac macGenerator = Mac.getInstance("HmacSHA256", "BC");
    macGenerator.init(new SecretKeySpec(macKey, "HmacSHA256"));
    byte[] expectedTag = macGenerator.doFinal(messaggio);
    //    if (!isArrayEqual(tag, expectedTag)) {
    //        throw new RuntimeException("Bad Message Authentication Code!");
    //      }

    // Decrypting the message.
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
    cipher.init(
        Cipher.DECRYPT_MODE,
        new SecretKeySpec(encryptionKey, "AES"),
    new IvParameterSpec(iv));
    return new String(cipher.doFinal(messaggio));
}
