String encrypt(String inputText) throws Exception {
    byte[] keyValue = new byte[] { 'm', 'y', 'k', 'e', 'y', 'n', 'u', 'l'};
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {           
        KeySpec keySpec = new DESKeySpec(keyValue);
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        IvParameterSpec iv = new IvParameterSpec(keyValue);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE,key,iv);
        bout.write(cipher.doFinal(inputText.getBytes("ASCII")));                        
    } catch(Exception e) {
        System.out.println("Exception .. "+ e.getMessage());
    }
    return new String(Base64.encodeBase64(bout.toByteArray()),"ASCII");
}

String decrypt(String inputText) throws Exception {
    byte[] keyValue = new byte[] { 'm', 'y', 'k', 'e', 'y', 'n', 'u', 'l'};
    try {
        KeySpec keySpec = new DESKeySpec(keyValue);
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
        IvParameterSpec iv = new IvParameterSpec(keyValue);

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,key,iv);
        //byte[] decoded = Base64.decodeBase64(inputText); //Works with apache.commons.codec-1.8
        byte[] decoded = Base64.decodeBase64(inputText.getBytes("ASCII"));// works with apache.commons.codec-1.3
        bout.write(cipher.doFinal(decoded));
    } catch(Exception e) {
        System.out.println("Exception ... "+e);
    }
    return new String(bout.toByteArray(),"ASCII");
}
