public class AES2 {
    private static final String algo = "AES/CBC/PKCS5Padding";
    private static final byte[] keyValue = 
        new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

    public static String encrypt(String Data) throws Exception {

        //KeyGenerator KeyGen = KeyGenerator.getInstance("AES");
        //KeyGen.init(128);

        //SecretKey SecKey = KeyGen.generateKey();
        Key key=generateKey();

        //generar IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        //cifrar
        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();

        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher c = Cipher.getInstance(algo);
        c.init(Cipher.DECRYPT_MODE, key,ivParameterSpec);

        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        return key;
}
}

Main class:

public class aesMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        String texto = "hola mama";
        String encryptedText=AES2.encrypt(texto);
        String decryptedText=AES2.decrypt(encryptedText);


        System.out.println("Plain text : " + texto);
        System.out.println("Encrypted Text : " + encryptedText);
        System.out.println("Decrypted Text : " + decryptedText);
    }

}
