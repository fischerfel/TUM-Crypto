private static void test() throws Exception {

    // create wrap key
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AESWrap");
    keyGenerator.init(256);
    Key wrapKey = keyGenerator.generateKey();

    SecretKey key = generateKey(PASSPHRASE);
    Cipher cipher;

    // wrap key
    cipher = Cipher.getInstance("AESWrap");
    cipher.init(Cipher.WRAP_MODE, wrapKey);
    byte[] wrappedKeyBytes = cipher.wrap(key);

    // unwrap key again
    cipher.init(Cipher.UNWRAP_MODE, wrapKey);
    key = (SecretKey)cipher.unwrap( wrappedKeyBytes, "AES/CTR/NOPADDING", Cipher.SECRET_KEY);

    // encrypt
    cipher = Cipher.getInstance("AES/CTR/NOPADDING");
    cipher.init(Cipher.ENCRYPT_MODE, key, generateIV(cipher), random);
    byte[] b = cipher.doFinal("Test".toString().getBytes());

    // decrypt
    cipher = Cipher.getInstance("AES/CTR/NOPADDING");
    cipher.init(Cipher.DECRYPT_MODE, key, generateIV(cipher), random);
    b = cipher.doFinal(b);

    System.out.println(new String(b));  
    // should output "Test", but outputs �J�� if wrapping/unwrapping

}
