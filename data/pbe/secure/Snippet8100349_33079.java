public SecretKey generateKey(String key) {
    this._paramSpec = new PBEParameterSpec(this.SALT, this.ITERATION_COUNT);
    PBEKeySpec spec = new PBEKeySpec(key.toCharArray());
    SecretKeyFactory fac = null;
    try {
        fac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
        System.err.println("[ERR] Cryptographer could not create a SecretKeyFactory due to an unsupported algorithm.");
    }
    try {
        if (fac == null)
            return null;
        return fac.generateSecret(spec);
    } catch (InvalidKeySpecException ex) {
        System.err.println("[ERR] Cryptographer could not generate a SecretKey due to an invalid Key Specification.");
        ex.printStackTrace();
        return null;
    }
}
