try {
    final byte[] alicePubKeyEnc = Util.fromBase64("BASE_64_PUBLIC_KEY_FROM_PUSH_SUBSCRIPTION");
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
    ECGenParameterSpec kpgparams = new ECGenParameterSpec("secp256r1");
    kpg.initialize(kpgparams);

    ECParameterSpec params = ((ECPublicKey) kpg.generateKeyPair().getPublic()).getParams();
    final ECPublicKey alicePubKey = fromUncompressedPoint(alicePubKeyEnc, params);
    KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("EC");
    bobKpairGen.initialize(params);

    KeyPair bobKpair = bobKpairGen.generateKeyPair();
    KeyAgreement bobKeyAgree = KeyAgreement.getInstance("ECDH");
    bobKeyAgree.init(bobKpair.getPrivate());


    byte[] bobPubKeyEnc = toUncompressedPoint((ECPublicKey) bobKpair.getPublic());


    bobKeyAgree.doPhase(alicePubKey, true);
    Cipher bobCipher = Cipher.getInstance("AES/GCM/NoPadding");
    SecretKey bobDesKey = bobKeyAgree.generateSecret("AES");
    byte[] saltBytes = new byte[16];
    new SecureRandom().nextBytes(saltBytes);
    Mac extract = Mac.getInstance("HmacSHA256");
    extract.init(new SecretKeySpec(saltBytes, "HmacSHA256"));
    final byte[] prk = extract.doFinal(bobDesKey.getEncoded());

    // Expand
    Mac expand = Mac.getInstance("HmacSHA256");
    expand.init(new SecretKeySpec(prk, "HmacSHA256"));
    String info = "Content-Encoding: aesgcm128";
    expand.update(info.getBytes(StandardCharsets.US_ASCII));
    expand.update((byte) 1);
    final byte[] key_bytes = expand.doFinal();

    // Use the result
    SecretKeySpec key = new SecretKeySpec(key_bytes, 0, 16, "AES");
    bobCipher.init(Cipher.ENCRYPT_MODE, key);

    byte[] cleartext = "{\"this\":\"is a test that is supposed to be working but it is not\"}".getBytes();
    byte[] ciphertext = bobCipher.doFinal(cleartext);

    URL url = new URL("PUSH_ENDPOINT_URL");
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    urlConnection.setRequestMethod("POST");
    urlConnection.setRequestProperty("Content-Length", ciphertext.length + "");
    urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
    urlConnection.setRequestProperty("Encryption-Key", "keyid=p256dh;dh=" + Util.toBase64UrlSafe(bobPubKeyEnc));
    urlConnection.setRequestProperty("Encryption", "keyid=p256dh;salt=" + Util.toBase64UrlSafe(saltBytes));
    urlConnection.setRequestProperty("Content-Encoding", "aesgcm128");
    urlConnection.setDoInput(true);
    urlConnection.setDoOutput(true);
    final OutputStream outputStream = urlConnection.getOutputStream();
    outputStream.write(ciphertext);
    outputStream.flush();
    outputStream.close();
    if (urlConnection.getResponseCode() == 201) {
        String result = Util.readStream(urlConnection.getInputStream());
        Log.v("PUSH", "OK: " + result);
    } else {
        InputStream errorStream = urlConnection.getErrorStream();
        String error = Util.readStream(errorStream);
        Log.v("PUSH", "Not OK: " + error);
    }
} catch (Exception e) {
    Log.v("PUSH", "Not OK: " + e.toString());
}
