package test;

import java.security.AlgorithmParameters;
import java.security.spec.KeySpec;
import java.util.Formatter;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * Issue: Using "AES/CBC/PKCS5Padding" encryption, the Initialization Vector
 *        appears to only affect the first block?!? 
 * 
 * Example Output
 *    iv 1e6376d5d1180cf9fcf7c78d7f1f1b96
 *    bv 00000000000000000000000000000000
 *    I: 222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
 *    E: b35b3945cdcd08e2f8a65b353ff754c32a48d9624e16b616d432ee5f78a26aa295d83625634d1048bf2dbb51fc657b7f796b60066129da5e1e7d3c7b51a30c1d962db75ac6666d4b32513c154b47f18eb66f62d7417cfd77f07f81f27f08d7d818e6910ca5849da3e6cff852bc06317e2d51907879598c8d3ae74074f4c27f7b8e2f74ca04d3ed6ac839b819a0f4cb462d0a4d9497cd917b8bd0aafb590ddd593b5b652cf8f642d3b2cd9dc0981dc1c913d52d065a844ea65e72cd7738eee3b488c4304e884109320dc54668ac4659d6014de9cf19422f7f68157d4330478589533571434d07b1939e56259fb8828823361bc912b84dc6ccdd5878b1d05801e0a6ce099bc86f1356fd145338163d59a07f2efdb1a6f91f4a35e6304f2d15d9972b0dda3c2275b5942a7f032ab6f90138
 *    D: 3c4154f7f33a2edbded5e5af5d3d39b42222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222
 */
public class IvBug {

    public static void main(String[] args) throws Exception {
        // Initialize.
        final char[] password = "foo".toCharArray();
        final byte[] salt = "bar".getBytes();

        byte[] iData = new byte[300];
        java.util.Arrays.fill(iData, (byte)0x22);               // Make the issue easy to see.
//      for (int i=0; i<msg.length; i++) msg[i] = (byte) i;     // Alternate fill.

        // Perform the test.
        SecretKey sKey = generateKey(password,salt);
        byte[] iv = generateIv(sKey);
        byte[] eData = encrypt(sKey, iData, iv);
        byte[] badIv = new byte[iv.length];             // Discard initialization vector.
        byte[] dData = decrypt(sKey, eData, badIv);

        // Display the results.
        System.out.println("iv " + hexStr(iv));
        System.out.println("bv " + hexStr(badIv));
        System.out.println("I: " + hexStr(iData));      // Initial
        System.out.println("E: " + hexStr(eData));      // Encrypted
        System.out.println("D: " + hexStr(dData));      // Decrypted
    }

    static SecretKey generateKey(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    static byte[] generateIv(SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameters params = cipher.getParameters();
        return params.getParameterSpec(IvParameterSpec.class).getIV();
    }

    static byte[] encrypt(SecretKey key, byte[] data, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    static byte[] decrypt(SecretKey key, byte[] data, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    static String hexStr(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) formatter.format("%02x", b);
            return formatter.toString();
        }
    }
}
