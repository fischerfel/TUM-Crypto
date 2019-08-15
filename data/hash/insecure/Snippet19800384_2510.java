import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

public class WebSocket {

    private ServerSocket server;
    private Socket sock;
    private InputStream in;
    private OutputStream out;

    public WebSocket() {
    }

    public void listen(int port) throws IOException {

        server = new ServerSocket(port);
        sock = server.accept();
        server.close();

        in = sock.getInputStream();
        out = sock.getOutputStream();
    }

    private void handshake() throws Exception {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(in, "UTF8"));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(out, "UTF8"));

        // the first line of HTTP headers
        String line = br.readLine();

        if (!line.startsWith("GET"))
            throw new IOException("Wrong header: " + line);

        // we read header fields
        String key = null;

        // read line by line until we get empty line
        while (!(line = br.readLine()).isEmpty()) {

            if (line.toLowerCase().contains("sec-websocket-key")) {
                key = line.substring(line.indexOf(":") + 1).trim();
            }
        }

        if (key == null)
            throw new IOException("No Websocket key specified");

        System.out.println(key);

        // add key and magic value
        String accept = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

        // sha1
        byte[] digest = MessageDigest.getInstance("SHA-1").digest(
                accept.getBytes("UTF8"));
        // and base64
        accept = DatatypeConverter.printBase64Binary(digest);

        // send http headers
        pw.println("HTTP/1.1 101 Switching Protocols");
        pw.println("Upgrade: websocket");
        pw.println("Connection: Upgrade");
        pw.println("Sec-WebSocket-Accept: " + accept);
        pw.println();
        pw.flush();

    }

    private void send(String message) throws Exception {
        BufferedImage image = ImageIO.read(new File("image.jpg"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] byteArray = baos.toByteArray();

        out.write(byteArray);
        out.flush();
    }

    private void close() {
        try {
            sock.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /** throws Exception, because we don't really care much in this example */
    public static void main(String[] args) throws Exception {
        WebSocket ws = new WebSocket();

        System.out.println("Listening...");
        ws.listen(9000);

        System.out.println("Handshake");
        ws.handshake();

        System.out.println("Handshake complete!");

        ws.send("I got your message! It's length was");

        ws.close();
    }
}
