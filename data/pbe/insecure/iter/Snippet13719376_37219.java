class PBE{
    public PBE(String pw) {
        this.password = pw;
    }
    public SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // make password
        PBEKeySpec keySpec = new PBEKeySpec(this.password.toCharArray(),this.salt,20);
        // create key instance
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        // generate key
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
    }
}
