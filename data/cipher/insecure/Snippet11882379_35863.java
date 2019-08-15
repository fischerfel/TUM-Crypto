 public static String generate(String password, String passphase) throws Exception {
    try {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(passphase.toCharArray());
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;
        // Salt
        byte[] salt = {(byte) 0xc8, (byte) 0x73, (byte) 0x61, (byte) 0x1d, (byte) 0x1a, (byte) 0xf2, (byte) 0xa8, (byte) 0x99};
        // Iteration count
        int count = 20;
        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, count);
        keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        // Our cleartext
        byte[] cleartext = password.getBytes();
        // Encrypt the cleartext
        byte[] ciphertext = pbeCipher.doFinal(cleartext);
        return byteArrayToHexString(ciphertext).substring(0, 12);
    } catch (Exception ex) {
        throw new Exception(ex.getMessage());
    }
}

public static String byteArrayToHexString(byte[] b){
    StringBuilder sb = new StringBuilder(b.length * 2);
    for (int i = 0; i < b.length; i++){
        int v = b[i] & 0xff;
        if (v < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(v));
    }
    return sb.toString().toUpperCase();
}
