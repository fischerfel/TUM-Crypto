public class GCMTest {

    public static void main(String[] args) throws Exception {

        //***********************************************************
        //Key
        byte[] key = MessageDigest.getInstance("MD5").digest("1234567890123456".getBytes("UTF-8"));//this is the random key

        //Iv
        SecureRandom srand = SecureRandom.getInstance("SHA1PRNG");
        byte[] iv = new byte[256];
        srand.nextBytes(iv);

        //Input
        byte[] data="inputPlainText".getBytes();

        final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * Byte.SIZE, iv);

        //***********************************************************
        //Encryption
        final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), gcmParameterSpec);

        cipher.updateAAD("MyAAD".getBytes("UTF-8"));

        //Encrypted output
        final byte[] encrypted = new byte[cipher.getOutputSize(data.length)];
        cipher.update(data, 0, data.length, encrypted, 0);  //Not being updated for current data. 

        //Tag output
        byte[] tag = new byte[cipher.getOutputSize(data.length)];
        cipher.doFinal(tag, 0);


        //***********************************************************
        //Decryption
        final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        cipher.updateAAD("MyAAD".getBytes("UTF-8"));

        //What size should be assigned to outputBuffer?
        final byte[] data1 = new byte[256];

        int offset = cipher.update(encrypted, 0, encrypted.length, data1, 0);
        cipher.update(tag, 0, tag.length, data1, offset);
        cipher.doFinal(data1, offset);

        boolean isValid = checkEquals(data, data1);
        System.out.println("isValid :"+isValid);
    }

    private static boolean checkEquals(byte[] a, byte[] b)
    {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            diff |= a[i] ^ b[i];
        return diff == 0;
    }
}
