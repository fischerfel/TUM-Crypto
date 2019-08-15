public byte[] deriveKey(String p, byte[] s, int i, int l) throws Exception {

    PBEKeySpec ks = new PBEKeySpec(p.toCharArray(), s, i, l);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    return skf.generateSecret(ks).getEncoded();
}
