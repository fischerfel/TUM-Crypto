public class Server {

    private static SecretKeySpec AES_Key;
    private static final String key = "1234567890ABCDEF";
    public static PublicKey keycl;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InstantiationException, IllegalAccessException {

        AES_Key = new SecretKeySpec(key.getBytes(), "AES");
        //System.out.println(AES_Key); 

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4443);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4443.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String inputLine, outputLine;

        System.out.println("Server run ");

        int cnt = 0;

        byte final_plaintext[];

        byte[] AES = key.getBytes();

        PublicKey pubKey = readKeyFromFile("/publicClient.key");
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        final_plaintext = cipher.doFinal(AES);

        String m = new BASE64Encoder().encode(final_plaintext);


        System.out.println(m);
        out.println(m);

        while ((inputLine = in.readLine()) != null) {
            cnt++;
            Cipher AES_Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            AES_Cipher.init(Cipher.DECRYPT_MODE, AES_Key);
            byte[] input = new BASE64Decoder().decodeBuffer(inputLine);
            //System.out.println(input);
            byte plaintext_decrypted[] = AES_Cipher.doFinal(input);
            inputLine = new String(plaintext_decrypted, "UTF-8");

            System.out.println("Server receive : " + inputLine);
            System.out.println("type message :");
            outputLine = stdIn.readLine();

            AES_Cipher.init(Cipher.ENCRYPT_MODE, AES_Key);
            byte plaintext[] = outputLine.getBytes();
            final_plaintext = AES_Cipher.doFinal(plaintext);
             String msg = new BASE64Encoder().encode(final_plaintext);

            out.println(msg);
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static PublicKey readKeyFromFile(String keyFileName) throws IOException {
        InputStream in
                = Server.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin
                = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    public static byte[] rsaEncrypt(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey pubKey = readKeyFromFile("/privateServer.key");
        System.out.println(pubKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    }

    public static byte[] rsaDecrypt(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey pubKey = readKeyFromFile("/publicClient.key");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    }

    private static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len - 1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
}
