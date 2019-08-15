try {
    String publickey  = "MCwwDQYJKoZIhvcNAQEBBQADGwAwGAIRAK+dBpbOKw+1VKMWoFxjU6UCAwEAAQ==";
    byte[] bArr = Crypto.base64Decode(publicKey, false);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
    EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey);
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

    Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
    cipher.init(1,publicKey);
    int cipherBlockSize = cipher.getBlockSize();
    ByteArrayOutputStream bArrOut = new ByteArrayOutputStream();
    bArrOut.flush();
    int pos = 0;
    Log.i("ContentBufferLength", contentBuffer.length+"");

    while (true) {
        if (cipherBlockSize > contentBuffer.length - pos) {
            cipherBlockSize = contentBuffer.length - pos;
        }
        Log.i("CipherBlockSize", cipherBlockSize+"");
        byte[] tmp = cipher.doFinal(contentBuffer, pos, cipherBlockSize);
        bArrOut.write(tmp);
        pos += cipherBlockSize;
        if (contentBuffer.length <= pos) {
            break;
        }
    }
    bArrOut.flush();
    encryptedBuffer = bArrOut.toByteArray();
    bArrOut.close();
} catch (Exception ex) {
    throw ex;
}

//  Log.i("Encrypted Buffer Length", encryptedBuffer.length+"");
return encryptedBuffer;
