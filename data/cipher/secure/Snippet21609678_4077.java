public class AES {

    public static SecretKey generateAESKey(int bits) throws NoSuchAlgorithmException{
        //This method is provided as to securely generate a AES key of the given length.

        //In practice you can specify your own SecureRandom instance.
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(bits);
        return kgen.generateKey();
    }

    public static byte[] encrypt(SecretKey key, byte[] plaintext) throws Exception{
        //In practice you should specify your SecureRandom implementation.
        SecureRandom rnd = new SecureRandom();

        //Generate random IV of 128-bit (AES block size)
        byte[] IV = new byte[128 / 8]; 
        rnd.nextBytes(IV);
        IvParameterSpec IVSpec = new IvParameterSpec(IV);

        //Create the cipher object to perform AES operations.
        //Specify Advanced Encryption Standard - Cipher Feedback Mode - No Padding
        Cipher AESCipher = Cipher.getInstance("AES/CFB/NoPadding");

        //Initialize the Cipher with the key and initialization vector.
        AESCipher.init(Cipher.ENCRYPT_MODE, key, IVSpec);

        //Encrypts the plaintext data
        byte[] ciphertext = AESCipher.doFinal(plaintext);

       /*
        * The IV must now be transferred with the ciphertext somehow. The easiest 
        * way to accomplish this would be to prepend the IV to the ciphertext 
        * message.
        */

        //Allocate new array to hold ciphertext + IV
        byte[] output = new byte[ciphertext.length + (128 / 8)];

        //Copy the respective parts into the array.
        System.arraycopy(IV, 0, output, 0, IV.length);
        System.arraycopy(ciphertext, 0, output, IV.length, ciphertext.length);

        return output;
    }

    public static byte[] decrypt(SecretKey key, byte[] IV, byte[] ciphertext) throws Exception{
        //Create the cipher object to perform AES operations.
        //Specify Advanced Encryption Standard - Cipher Feedback Mode - No Padding
        Cipher AESCipher = Cipher.getInstance("AES/CFB/NoPadding");

        //Create the IvParameterSpec object from the raw IV
        IvParameterSpec IVSpec = new IvParameterSpec(IV);

        //Initialize the Cipher with the key and initialization vector.
        AESCipher.init(Cipher.DECRYPT_MODE, key, IVSpec);

        //Decrypts the ciphertext data
        byte[] plaintext = AESCipher.doFinal(ciphertext);

        return plaintext;
    }

    public static void main(String[] args) throws Exception{
        //Demo the program

        String sPlaintext = "rainbows"; //String plaintext
        byte[] rPlaintext = sPlaintext.getBytes(Charset.forName("UTF-8")); //Raw byte array plaintext

        //We first need to generate a key of 128-bit
        SecretKey key = generateAESKey(128);

        //Encrypt the plaintext
        byte[] output = encrypt(key, rPlaintext);

        // ----------------- //

        //Extract the IV from the encryption output
        byte[] IV = new byte[128 / 8];
        byte[] ciphertext = new byte[output.length - (128 / 8)];

        System.arraycopy(output, 0, IV, 0, IV.length);
        System.arraycopy(output, IV.length, ciphertext, 0, ciphertext.length);

        //Decrypt the ciphertext
        byte[] dPlaintext = decrypt(key, IV, ciphertext);

        String decryptedMessage = new String(dPlaintext, Charset.forName("UTF-8"));

        //Print stuff out
        System.out.println("Original message: " + sPlaintext);
        System.out.println("Original message bytes: " + Arrays.toString(rPlaintext));
        System.out.println("Encryption Output bytes: " + Arrays.toString(output));
        System.out.println("Decrypted message bytes: " + Arrays.toString(dPlaintext));
        System.out.println("Decrypted message: " + decryptedMessage);
    }
}
