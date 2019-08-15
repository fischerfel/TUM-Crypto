byte[] keyBytes = Base64.decodeBase64(PUB_KEY);
try {
        AlgorithmIdentifier rsaIdent = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption);
        SubjectPublicKeyInfo kInfo = new SubjectPublicKeyInfo(rsaIdent, keyBytes);
        ASN1Primitive primKey = kInfo.parsePublicKey();
        byte[] encoded = primKey.getEncoded();
        byte[] sessionBytes = Base64.decodeBase64("Zm/qR/FrkzawabBZYk7WfQJNMVZoZrwWTvfQwIhPMzAuqEO+y+sb/x9+TZwTbqmu45/GV4yhKv0bbDL8F6rif7RJap7iQUFQBDEIAraY42IGZ8pB6A0Q0RSnJWW+tLTLJg5cTrgZQ8sLoO+U03T6DE1wy73FU5h6XhXxZERo0tQ=");
        Security.addProvider(new BouncyCastleProvider());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
        KeyFactory factory = KeyFactory.getInstance(spec.getFormat());
        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.DECRYPT_MODE, factory.generatePublic(spec));
        // ----- THIS IS WHERE IT BREAKS -----
        byte[] decrypted = cipher.doFinal(sessionBytes);
        String tada = new String(decrypted, StandardCharsets.UTF_8);
} catch (Exception e) { ... }
