private void start() {
    OutputStream os = null;
    try {
        os = new FileOutputStream("/some/file");
        os = wrapAllRequiredTransforms(os);

        //Write to os
    } finally {
        os.close();
    }
}

private wrapAllRequiredTransforms(OutputStream os) {
    if(checkSumRequired) {
        os = wrapOStreamWithCheckSum(os);
    }

    if(encryptionRequired) {
        os = wrapOStreamWithCipher(os);
    }

    if(compressRequired) {
        os = wrapOStreamWithCompress(os);
    }
}

private OutputStream wrapOStreamWithCheckSum(OutputStream os) throws Exception {
    os = new DigestOutputStream(os, MessageDigest.getInstance("MD5"));
    return os;
}

private OutputStream wrapOStreamWithCipher(OutputStream os) throws Exception {
    SecretKeySpec secretKeySpec = new SecretKeySpec(//SomeKey, encryptionAlgorithm);
    Cipher cipher = Cipher.getInstance(encryptionAlgorithm); 
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    return new CipherOutputStream(os, cipher);
}

private OutputStream wrapOStreamWithCompress(OutputStream os) throws Exception {
    return new GZIPOutputStream(os);
}
