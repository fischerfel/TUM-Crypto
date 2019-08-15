public static void main(String args[]) throws Exception {
    KeyGenerator kg = KeyGenerator.getInstance("DESede");
    Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    PrivateKey key = getPrivateKey("path/DESedeRACE.key");

    c.init(Cipher.ENCRYPT_MODE, key);
    byte input[] = "Gufz74gXCm2UV3ux+D4/Bnp7Jop90EvfK3QCkt6ZhRmtKeRf9OxzNilY8Xcai1UUQ==".getBytes();
    byte encrypted[] = c.doFinal(input);
    byte iv[] = c.getIV();
    System.out.println(new String(iv));

    IvParameterSpec dps = new IvParameterSpec(iv);
    c.init(Cipher.DECRYPT_MODE, key, dps);
    byte output[] = c.doFinal(encrypted);
    System.out.println(new String(output));
} 
