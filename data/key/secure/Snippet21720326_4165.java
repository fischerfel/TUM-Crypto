    public static final String encrypt(String value, char[] passPhrase){
    if(value != null){
        try{
            SecretKeySpec skeySpec = new SecretKeySpec(SecureCrypto.generateSecretKey(passPhrase).getEncoded(), ENC_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENC_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            value = Base64.encodeToString(cipher.doFinal(value.getBytes("utf-8")), Base64.NO_WRAP);
        }catch(Throwable th){
            Environment.logError(Environment.APPLICATION_LOG_TAG, th);
        }
    }

    return value;
}
