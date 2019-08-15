enter code here

public static void main(String[] args)  {
//Same password used in android
        String masterpassword ="test";
        String crypto =  encrypt(masterpassword, "XYZ");

}






 public static String encrypt(String seed, String cleartext) throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            System.out.println(rawKey);
            byte[] result = encrypt(rawKey, cleartext.getBytes());
           return Base64.encode(result);
         //   return toHex(result);
        }




 private static byte[] getRawKey(byte[] seed) throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
        }




    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }
