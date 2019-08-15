private static MessageDigest md;
static {
    try {
        md = MessageDigest.getInstance("MD5");
    }
    catch(Exception e) {
        md = null;
    }
}

private static byte[] md5(String source) {
    byte[] bytes = source.getBytes();
    byte[] digest = md.digest(bytes);
    return digest;
}

public static String encrypt(String input, String key){
    byte[] ivbytes = "sixteenbyteslong".getBytes();  // <- NO NO NO NO  !!!!
    IvParameterSpec ips = new IvParameterSpec(ivbytes);
    System.out.println("plaintext: " + input);
    byte[] keybytes = md5(key);  // <- NO NO NO NO !!!!
    System.out.println("key      : " + Hex.encodeHexString(keybytes));
    System.out.println("iv       : " + Hex.encodeHexString(ivbytes));
    byte[] crypted = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey, ips);
        byte[] ptext = input.getBytes();
        crypted = cipher.doFinal(ptext);
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(Hex.encodeHexString(crypted));
}

public static String decrypt(String input, String key){
    IvParameterSpec ips = new IvParameterSpec("sixteenbyteslong".getBytes());  // <- NO !!!
    byte[] keybytes = md5(key);  // <- BAD BAD BAD!!!
    System.out.println("key      : " + Hex.encodeHexString(keybytes));
    byte[] output = null;
    try{
        SecretKeySpec skey = new SecretKeySpec(keybytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey, ips);
        output = cipher.doFinal(Hex.decodeHex(input.toCharArray()));
    }catch(Exception e){
        System.out.println(e.toString());
    }
    return new String(output);
}

public void Run() {
    String plaintext = CommandLineArgs.get("pt");
    String keystring = CommandLineArgs.get("k");

    if (plaintext == null || keystring == null) {
        Usage();
        return;
    }

    System.out.println("encrypting...");
    String crypto = encrypt(plaintext, keystring);
    System.out.println("crypto   : " + crypto);
    System.out.println("decrypting...");
    String decrypted = decrypt(crypto, keystring);
    System.out.println("decrypted: " + decrypted);
}
