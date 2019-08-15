Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
rsaCipher.init(Cipher.DECRYPT_MODE, rsaPk);

int chunkSize = 128;
int idx = 0;
ByteBuffer buf = ByteBuffer.allocate(data.length);
while(idx < data.length) {
    int len = Math.min(data.length-idx, chunkSize);
    byte[] chunk = rsaCipher.doFinal(data, idx, len);
    buf.put(chunk);
    idx += len;
}

// fully decrypted data
byte[] decryptedData = buf.array();
