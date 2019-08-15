public class CrytedClass {
    public static byte[] generateKey(String pass) throws Exception{
        byte [] start = pass.getBytes("UTF-8");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

        sr.setSeed(start);
        kgen.init(128,sr);
        SecretKey skey = kgen.generateKey();
        return skey.getEncoded();
    }

    public static byte[] encodedFile(byte[] key, byte[] fileData)throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,skeySpec);
        byte [] encrypted = cipher.doFinal(fileData);
        return encrypted;

    }
    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception{
        SecretKeySpec skeySpec = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,skeySpec);
        byte [] decrypted = cipher.doFinal(fileData);
        return decrypted;
    }

    public static String generatePass(){
        return new BigInteger(130, new SecureRandom()).toString(32);

    }
    public static byte[] createHas(byte[] ficheroEncrip){
        MessageDigest msd = null;
        try{
            msd = MessageDigest.getInstance("SHA-1");
        }catch (Exception e){
            return null;
        }
        msd.update(ficheroEncrip);
        return msd.digest();
    }
}
