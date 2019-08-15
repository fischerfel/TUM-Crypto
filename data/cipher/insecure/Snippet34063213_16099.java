public void setPassCode(String   value) throws Exception { 

    try {

        SecretKeySpec sks = null; 
        sks = getEncryptKey();

        byte[] userLatENC=null;

        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,sks ); 
        userLatENC = c.doFinal(value.getBytes());

        passCode = Base64.encodeToString(userLatENC, Base64.DEFAULT);

    } catch (Exception e) {
        throw e;
    }
}
