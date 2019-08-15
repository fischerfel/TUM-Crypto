Cipher ecipher;
try {
    ecipher = Cipher.getInstance("DES");
    SecretKeySpec keySpec = new SecretKeySpec(key, "DES");      
    ecipher.init(Cipher.ENCRYPT_MODE, keySpec);         
    byte[] utf8 = password.getBytes("UTF8");
    byte[] enc = ecipher.doFinal(utf8);
    return new sun.misc.BASE64Encoder().encode(enc);
}
catch {
    // ...
}
