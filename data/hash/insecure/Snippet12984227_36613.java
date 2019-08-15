package javaapplication5;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import sun.misc.BASE64Encoder;

/**
 *
 * @author
 * Anders
 */
public class WebsocketServer {

    public static final int MASK_SIZE = 4;
    public static final int SINGLE_FRAME_UNMASKED = 0x81;
    private ServerSocket serverSocket;
    private Socket socket;

    public WebsocketServer() throws IOException {
    serverSocket = new ServerSocket(2005);
    connect();
    }

    private void connect() throws IOException {
    System.out.println("Listening");
    socket = serverSocket.accept();
    System.out.println("Got connection");
    if(handshake()) {
        listenerThread();
    }
    }

    private boolean handshake() throws IOException {
    PrintWriter out = new PrintWriter(socket.getOutputStream());
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    HashMap<String, String> keys = new HashMap<>();
    String str;
    //Reading client handshake
    while (!(str = in.readLine()).equals("")) {
        String[] s = str.split(": ");
        System.out.println();
        System.out.println(str);
        if (s.length == 2) {
        keys.put(s[0], s[1]);
        }
    }
    //Do what you want with the keys here, we will just use "Sec-WebSocket-Key"

    String hash;
    try {
        hash = new BASE64Encoder().encode(MessageDigest.getInstance("SHA-1").digest((keys.get("Sec-WebSocket-Key") + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));
    } catch (NoSuchAlgorithmException ex) {
        ex.printStackTrace();
        return false;
    }

    //Write handshake response
    out.write("HTTP/1.1 101 Switching Protocols\r\n"
        + "Upgrade: websocket\r\n"
        + "Connection: Upgrade\r\n"
        + "Sec-WebSocket-Accept: " + hash + "\r\n"
        + "\r\n");
    out.flush();

    return true;
    }

    private byte[] readBytes(int numOfBytes) throws IOException {
    byte[] b = new byte[numOfBytes];
    socket.getInputStream().read(b);
    return b;
    }

    public void sendMessage(byte[] msg) throws IOException {
    System.out.println("Sending to client");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
    baos.write(SINGLE_FRAME_UNMASKED);
    baos.write(msg.length);
    baos.write(msg);
    baos.flush();
    baos.close();
    convertAndPrint(baos.toByteArray());
    os.write(baos.toByteArray(), 0, baos.size());
    os.flush();
    }

    public void listenerThread() {
    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
        try {
            while (true) {
            System.out.println("Recieved from client: " + reiceveMessage());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        }
    });
    t.start();
    }

    public String reiceveMessage() throws IOException {
    byte[] buf = readBytes(2);
    System.out.println("Headers:");
    convertAndPrint(buf);
    int opcode = buf[0] & 0x0F;
    if (opcode == 8) {
        //Client want to close connection!
        System.out.println("Client closed!");
        socket.close();
        System.exit(0);
        return null;
    } else {
        final int payloadSize = getSizeOfPayload(buf[1]);
        System.out.println("Payloadsize: " + payloadSize);
        buf = readBytes(MASK_SIZE + payloadSize);
        System.out.println("Payload:");
        convertAndPrint(buf);
        buf = unMask(Arrays.copyOfRange(buf, 0, 4), Arrays.copyOfRange(buf, 4, buf.length));
        String message = new String(buf);
        return message;
    }
    }

    private int getSizeOfPayload(byte b) {
    //Must subtract 0x80 from masked frames
    return ((b & 0xFF) - 0x80);
    }

    private byte[] unMask(byte[] mask, byte[] data) {
    for (int i = 0; i < data.length; i++) {
        data[i] = (byte) (data[i] ^ mask[i % mask.length]);
    }
    return data;
    }

    private void convertAndPrint(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(String.format("%02X ", b));
    }
    System.out.println(sb.toString());
    }

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException {
    WebsocketServer j = new WebsocketServer();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
        System.out.println("Write something to the client!");
        j.sendMessage(br.readLine().getBytes());
    }
    }
}
