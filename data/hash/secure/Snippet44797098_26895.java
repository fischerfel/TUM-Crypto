private boolean validatePinning(HttpsURLConnection conn, Set<String> validPins) {
    try {
        Certificate[] certs = conn.getServerCertificates();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (Certificate cert : certs) {
            X509Certificate x509Certificate = (X509Certificate) cert;
            byte[] key = x509Certificate.getPublicKey().getEncoded();
            md.update(key, 0, key.length);
            byte[] hashBytes = md.digest();
            StringBuffer hexHash = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                int k = 0xFF & hashBytes[i];
                String tmp = (k<16)? "0" : "";
                tmp += Integer.toHexString(0xFF & hashBytes[i]);
                hexHash.append(tmp);
            }
            if (validPins.contains(hexHash.toString())) {
                return true;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    return false;
}
