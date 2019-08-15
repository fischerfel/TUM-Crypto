public class BCTest {
    public static void doTest() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        byte[] clearData = "1234567890".getBytes();
        SecretKey secretKey = new SecretKeySpec("0123456789ABCDEF".getBytes(), "AES");
        AlgorithmParameterSpec IVspec = new IvParameterSpec("0123456789ABCDEF".getBytes());

        // encrypt with PKCS7 padding
        Cipher encrypterWithPad = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
        encrypterWithPad.init(Cipher.ENCRYPT_MODE, secretKey, IVspec);
        byte[] encryptedData = encrypterWithPad.doFinal(clearData);
        System.out.println("Encryped data (" + encryptedData.length + " bytes): \t" + toHexString(encryptedData));

        // decrypt with PKCS7 pad
        Cipher decrypterWithPad = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
        decrypterWithPad.init(Cipher.DECRYPT_MODE, secretKey, IVspec);
        byte[] buffer1 = new byte[encryptedData.length]; 
        int decryptLen1 = decrypterWithPad.doFinal(encryptedData, 0, encryptedData.length, buffer1); 
        System.out.println("Decrypted with Pad (" + decryptLen1 + " bytes):  \t" + toHexString(buffer1));

        // decrypt without PKCS7 pad
        Cipher decrypterWithoutPad = Cipher.getInstance("AES/CBC/NOPADDING", "BC");
        decrypterWithoutPad.init(Cipher.DECRYPT_MODE, secretKey, IVspec);
        byte[] buffer2 = new byte[encryptedData.length]; 
        int decryptLen2 = decrypterWithoutPad.doFinal(encryptedData, 0, encryptedData.length, buffer2); 
        System.out.println("Decrypted without Pad (" + decryptLen2 + " bytes):\t" + toHexString(buffer2));
    }

    private static String toHexString(byte[] bytes) {
        return javax.xml.bind.DatatypeConverter.printHexBinary(bytes);
    }

    public static void main(String[] args) throws Exception {
        BCTest.doTest(); 
    }
}
