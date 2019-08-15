    public class Encryption extends Applet {
    Key keyOrig;
    BASE64Decoder decoder = new BASE64Decoder();
    BASE64Encoder encoder = new BASE64Encoder();

    public void init() {
        try {
            keyOrig = generateKey();

            String keyString = encoder.encode(keyOrig.getEncoded());
            System.out.println("Key: "+keyString);

            Key key = new SecretKeySpec(keyString.getBytes(),0,keyString.getBytes().length, "DES");     

            String message = "This is hacker proof!";
            System.out.println("Message is: "+message);

            String encryptedMessage = encrypt(message,key);
            System.out.println("Message encrypted: "+ encryptedMessage);

            String decryptedMessage = decrypt(encryptedMessage,key);
            System.out.println("Message decrypted: "+ decryptedMessage);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Key generateKey() throws NoSuchAlgorithmException {
        KeyGenerator generator;
        generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        return keyOrig = generator.generateKey();
    }

    @SuppressWarnings("unused")
    public String encrypt(String message, Key key)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, UnsupportedEncodingException {
        // Get a cipher object.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Gets the raw bytes to encrypt, UTF8 is needed for
        // having a standard character set
        byte[] stringBytes = message.getBytes("UTF8");

        // encrypt using the cypher
        byte[] raw = cipher.doFinal(stringBytes);

        // converts to base64 for easier display.
        @SuppressWarnings("restriction")
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(raw);

        return base64;
    }

    public String decrypt(String encrypted, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        // Get a cipher object.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // decode the BASE64 coded message
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] raw = decoder.decodeBuffer(encrypted);

        // decode the message
        byte[] stringBytes = cipher.doFinal(raw);

        // converts the decoded message to a String
        String clear = new String(stringBytes, "UTF8");
        return clear;
    }
}
