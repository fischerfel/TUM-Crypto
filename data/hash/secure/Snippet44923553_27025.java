Certificate[] certs = conn.getServerCertificates();
MessageDigest md = MessageDigest.getInstance("SHA-256");
for (Certificate cert : certs) {
    X509Certificate x509Certificate = (X509Certificate) cert;
    byte[] key = x509Certificate.getPublicKey().getEncoded();
    md.update(key, 0, key.length);
    byte[] hashBytes = md.digest();
    StringBuilder hexHash = new StringBuilder();
    for (byte hashByte : hashBytes) {
        int k = 0xFF & hashByte;
        String tmp = (k < 16) ? "0" : "";
        tmp += Integer.toHexString(0xFF & hashByte);
        hexHash.append(tmp);
    }
    // You can even log cert.toString()
    Log.d(TAG, hexHash.toString()); get the hash from here
    // If you create `pins` as a Set with all the valid pins from above
    if (pins.contains(hexHash.toString())) {
        return true;
    }
}
