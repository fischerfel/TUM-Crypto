    String key = "987654321";
    SecretKeySpec keyspec = new SecretKeySpec(getHash(key), "AES");
    Cipher cipherDecode = Cipher.getInstance("AES/ECB/ZeroBytePadding");
    byte[] text = Base64.decode(
            "wdRe00YxTFGQ65QmWukPxFLlZRSPqmRY8tHufikBHW0=",
            Base64.DEFAULT);
    cipherDecode.init(Cipher.DECRYPT_MODE, keyspec);

    final byte[] decrypted = cipherDecode.doFinal(text);

    String decyptedText = new String(decrypted);
