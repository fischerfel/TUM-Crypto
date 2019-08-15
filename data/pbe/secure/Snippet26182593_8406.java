public static SecretKeySpec generateKey(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password, salt, 1024, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        return secret;
    }

public static Map encrypt(String cleartext, byte[] iv, SecretKeySpec secret) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    // If the IvParameterSpec argument is omitted (null), a new IV will be
    // created
    cipher.init(Cipher.ENCRYPT_MODE, secret, iv == null ? null : new IvParameterSpec(iv));
    AlgorithmParameters params = cipher.getParameters();
    byte[] usediv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] ciphertext = cipher.doFinal(cleartext.getBytes("UTF-8"));
    Map result = new HashMap();
    result.put(IV, usediv);
    result.put(CIPHERTEXT, ciphertext);
    return result;
}


public static String decrypt(byte[] ciphertext, byte[] iv, SecretKeySpec secret) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
    String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
    return plaintext;
}

public static void main(String arg) throws Exception {
    byte[] salt = new byte[] { -11, 84, 126, 65, -87, -104, 120, 33, -89, 19, 57, -6, -27, -19, -101, 107 };



    byte[] interop_iv = Base64.decode("xxxxxxxxxxxxxxx==", Base64.DEFAULT);
    byte[] iv = null;
    byte[] ciphertext;
    SecretKeySpec secret; 
    secret = generateKey("xxxxxxxxxxxxxxx".toCharArray(), salt);
    Map result = encrypt(arg, iv, secret);
    ciphertext = (byte[]) result.get(CIPHERTEXT);
    iv = (byte[]) result.get(IV);
    System.out.println("Cipher text:" + Base64.encode(ciphertext, Base64.DEFAULT));
    System.out.println("IV:" + Base64.encode(iv, Base64.DEFAULT) + " (" + iv.length + "bytes)");
    System.out.println("Key:" + Base64.encode(secret.getEncoded(), Base64.DEFAULT));
    System.out.println("Deciphered: " + decrypt(ciphertext, iv, secret));

    // Interop demonstration. Using a fixed IV that is used in the C#
    // example
    result = encrypt(arg, interop_iv, secret);
    ciphertext = (byte[]) result.get(CIPHERTEXT);
    iv = (byte[]) result.get(IV);

    String text = Base64.encodeToString(ciphertext, Base64.DEFAULT);

    System.out.println();
    System.out.println("--------------------------------");
    System.out.println("Interop test - using a static IV");
    System.out.println("The data below should be used to retrieve the secret message by the receiver");
    System.out.println("Cipher text:  " + text);
    System.out.println("IV:           " + Base64.encodeToString(iv, Base64.DEFAULT));
    decrypt(Base64.decode(text, Base64.DEFAULT), iv, secret);
}
