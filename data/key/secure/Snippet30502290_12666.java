public class Client {

    private static SecretKeySpec AES_Key;
    private static String key;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        Socket mySocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            mySocket = new Socket("localhost", 4443);
            out = new PrintWriter(mySocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser, m1;
        byte[] input;
        System.out.println("Client run ");
        int cnt = 0;
        while (true) {
            // fromServer = in.readLine();
            m1 = in.readLine();
            //input=in.readLine().getBytes();
            if (m1 != null && cnt < 1) {
                cnt++;

                byte[] m = new BASE64Decoder().decodeBuffer(m1);
                PrivateKey privKey = readKeyFromFile("/privateClient.key");
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privKey);
                byte[] plaintext_decrypted = cipher.doFinal(m);

                fromServer = new String(plaintext_decrypted, "ASCII");

                AES_Key = new SecretKeySpec(fromServer.getBytes(), "AES");
                System.out.println("RSA communication ends");
                System.out.println("AES communication established");
            }
            System.out.println("type message :");

            //Encrypt msg to send
            Cipher AES_Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");
            fromUser = stdIn.readLine();
            AES_Cipher.init(Cipher.ENCRYPT_MODE, AES_Key);
            byte plaintext[] = fromUser.getBytes();
            byte[] final_plaintext = AES_Cipher.doFinal(plaintext);
            String msg = new BASE64Encoder().encode(final_plaintext);

            if (fromUser != null) {
                out.println(fromUser);
            } else {
                break;
            }

            fromServer = in.readLine();
            if (fromServer != null) {
                // Decrypt the message received
                AES_Cipher.init(Cipher.DECRYPT_MODE, AES_Key);
                input = new BASE64Decoder().decodeBuffer(fromServer);
                byte plaintext_decrypted[] = AES_Cipher.doFinal(input);
                fromServer = new String(plaintext_decrypted, "ASCII");

                System.out.println("Client receive :" + fromServer);
            } else {
                break;
            }
        }

        out.close();
        in.close();
        stdIn.close();
        mySocket.close();
    }

    static PrivateKey readKeyFromFile(String keyFileName) throws IOException {
        InputStream in
                = Client.class.getResourceAsStream(keyFileName);
        ObjectInputStream oin
                = new ObjectInputStream(new BufferedInputStream(in));
        try {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privKey = fact.generatePrivate(keySpec);
            return privKey;
        } catch (Exception e) {
            throw new RuntimeException("Spurious serialisation error", e);
        } finally {
            oin.close();
        }
    }

    static PublicKey readKeyFromFilePb(String keyFileName) throws IOException {
        InputStream in
                = Client.class.getResourceAsStream(keyFileName);
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

    static byte[] rsaDecrypt(byte[] in) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PrivateKey privKey = readKeyFromFile("/publicServer.key");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] cipherData = cipher.doFinal(in);
        return cipherData;
    }

    static byte[] rsaEncrypt(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey pubKey = readKeyFromFilePb("/privateClient.key");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        return cipherData;
    }

    private String toHexString(byte[] block) {
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
    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
}
