public class Client {

private static SecretKeySpec AES_Key;
private static final String key = "1234567890ABCDEF";

public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

    Socket mySocket = null;
    PrintWriter out = null;
    BufferedReader in = null;



    AES_Key = new SecretKeySpec(key.getBytes(), "AES");

    System.out.println(AES_Key);
     Cipher AES_Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding", "BC");

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
    String fromUser;

    System.out.println("Client run ");

    while (true) {
        System.out.println("type message :");
         AES_Cipher.init(Cipher.ENCRYPT_MODE, AES_Key);
        fromUser = stdIn.readLine();

        byte plaintext[] = fromUser.getBytes();
        byte final_plaintext[] = AES_Cipher.doFinal(plaintext);
       // fromUser=toHexString(final_plaintext);
       String msg = new String(final_plaintext, "ASCII");

        System.out.println(final_plaintext);
    if (fromUser != null) {
            out.println(msg);
    }
         else{ break; }

        fromServer = in.readLine();
        if(fromServer!=null){
            System.out.println("Client receive :" + fromServer);
        }
        else{  break; }
    }

    out.close();
    in.close();
    stdIn.close();
    mySocket.close();
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
