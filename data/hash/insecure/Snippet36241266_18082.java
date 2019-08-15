import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

public class DateServer {

    static String[] headers = new String[10];
    static String[] bodies = new String[10];

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        ServerSocket listener = new ServerSocket(9091);
        try {
            while (true) {
                Socket socket = listener.accept();
                System.out.println("Accepted");
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg;

                while ((msg = input.readLine()) != null) {
                    if (msg.length() == 0)
                      break;

                    parseHeaders(msg);

                }

                String httpResponse = returnHeader();

                try {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    out.println(httpResponse + "\n");
                    out.flush();
                    System.out.println("Sent HTTP Response");
                } finally {

                }
            }
        }
        finally {
            listener.close();
        }
    }

    public static void parseHeaders(String in) {
        System.out.println(in);
        String[] thisLine = in.split(": ");

        if (thisLine[0].equals("Host")) {
            bodies[0] = thisLine[1];
        }
        else if (thisLine[0].equals("Upgrade")) {
            bodies[1] = thisLine[1];
        }
        else if (thisLine[0].equals("Connection")) {
            bodies[2] = thisLine[1];
        }
        else if (thisLine[0].equals("Sec-WebSocket-Key")) {
            bodies[3] = thisLine[1];
        }
        else if (thisLine[0].equals("Sec-WebSocket-Version")) {
            bodies [4] = thisLine[1];
        }
    }

    public static String returnHeader() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String head = "HTTP/1.1 101 Web Socket Protocol Handshake\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: ";

        String key = bodies[3];
        String encoded = WebSocketAccept(key);

        String httpResponse = head.concat(encoded);
        httpResponse.concat("\r\n\r\n"); // is concat not working??
        return httpResponse;
    }

    private static String WebSocketAccept(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        message = message.concat("258EAFA5-E914-47DA-95CA-C5AB0DC85B11");
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));
        return convertByteArrayToHexString(hashedBytes);
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return convertHexToBase64(stringBuffer.toString());
    }

    private static String convertHexToBase64(String hex) {
        byte[] hexToBytes = DatatypeConverter.parseHexBinary(hex);
        String base64 = Base64.getEncoder().encodeToString(hexToBytes);
        System.out.println(base64);
        return base64; 
    }
}
