try {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
} catch(GeneralSecurityException e) {
    throw new IllegalStateException("Could not retrieve AES cipher", e);
} 
