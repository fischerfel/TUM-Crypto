private static String encryptPW(String pw){
    byte[] pwBytes = pw.getBytes();
    byte[] keyBytes = "0123456789abcdef".getBytes();
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    try {
        Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ciph.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = ciph.doFinal(pwBytes);
        pw = Base64.getEncoder().encodeToString(ciph.doFinal(pwBytes));

        for (byte b : encryptedBytes){
            System.out.print(b);
        }
        System.out.println();
    } catch (Exception e){
        e.printStackTrace();
    }
    return pw;
}

private static String decryptPW(String pw){
    byte[] pwBytes = Base64.getDecoder().decode(pw.getBytes());
    for (byte b : pwBytes){
        System.out.print(b);
    }
    System.out.println();

    byte[] keyBytes = "0123456789abcdef".getBytes();
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    try {
        Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
        ciph.init(Cipher.DECRYPT_MODE, keySpec, ciph.getParameters());
        pw = new String(ciph.doFinal(pwBytes));    
    } catch (Exception e){
        e.printStackTrace();
    }
    return pw;
}
