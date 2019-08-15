byte[] encData = null;
try {

    // create public key
    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(key);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey pk = kf.generatePublic(publicKeySpec);

    Cipher pkCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    pkCipher.init(Cipher.ENCRYPT_MODE, pk);

    int chunkSize = 117; // 1024 / 8 - 11(padding) = 117
    int encSize = (int) (Math.ceil(data.length/117.0)*128);
    int idx = 0;
    ByteBuffer buf = ByteBuffer.allocate(encSize);
    while (idx < data.length) {
        int len = Math.min(data.length-idx, chunkSize);
        byte[] encChunk = pkCipher.doFinal(data, idx, len);
        buf.put(encChunk);
        idx += len;
    }

    // fully encrypted data     
    encData = buf.array();
} catch (Exception e) {
    e.printStackTrace();
