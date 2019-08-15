public class Server {

    private static SecretKeySpec AES_Key;
    private static final String key = "1234567890ABCDEF";

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {


        AES_Key = new SecretKeySpec(key.getBytes(), "AES");

        System.out.println(AES_Key);

         Cipher AES_Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");

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
        byte[] input;
        System.out.println("Server run ");

        while ((input = in.readLine().getBytes()) != null) {

            AES_Cipher.init(Cipher.DECRYPT_MODE, AES_Key);


            System.out.println(input);
             byte plaintext_decrypted[] = AES_Cipher.doFinal(input);
            inputLine= toHexString(plaintext_decrypted);
            System.out.println("Server receive : "+inputLine);
            System.out.println("type message :");
             outputLine = stdIn.readLine();
             out.println(outputLine);
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
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
