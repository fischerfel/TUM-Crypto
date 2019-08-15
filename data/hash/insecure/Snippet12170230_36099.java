import java.net.*;
import java.io.*;
import java.security.*;

public class WebListener
{
    public static void main(String[] args) throws Exception
    {
    ServerSocket serverSocket = null;
    boolean listening = true;

    try {
        serverSocket = new ServerSocket(4444);
    } catch (IOException e) {
        System.err.println("Could not listen on port: 4444.");
        System.exit(-1);
    }

    while (listening) new ServerThread(serverSocket.accept()).start();

    serverSocket.close();
    }
}

class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
    super("ServerThread");
    this.socket = socket;
    }

    public void run() {

    try {
        OutputStream outStream = null;
        PrintWriter out = new PrintWriter( outStream = socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream()));

        String inputLine, outputLine;

        //Handle the headers first
        doHeaders( out, in );

        ..elided..

        out.close();
        in.close();
        socket.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void doHeaders(PrintWriter out, BufferedReader in) throws Exception
    {
    String inputLine = null;
    String key = null;

    //Read the headers
    while ( ( inputLine = in.readLine() ) != null )
    {
        //Get the key
        if ( inputLine.startsWith( "Sec-WebSocket-Key" ) ) key = inputLine.substring( "Sec-WebSocket-Key: ".length() );

        //They're done
        if ( inputLine.equals( "" ) ) break;
    }

    //We need a key to continue
    if ( key == null ) throw new Exception( "No Sec-WebSocket-Key was passed!" );

    //Send our headers
    out.println( "HTTP/1.1 101 Web Socket Protocol Handshake\r" );
    out.println( "Upgrade: websocket\r" );
    out.println( "Connection: Upgrade\r" );
    out.println( "Sec-WebSocket-Accept: " + createOK( key ) + "\r" );
    out.println( "\r" );
    }

    public String createOK(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception
    {
    String uid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    String text = key + uid;

    MessageDigest md = MessageDigest.getInstance( "SHA-1" );
    byte[] sha1hash = new byte[40];
    md.update( text.getBytes("iso-8859-1"), 0, text.length());
    sha1hash = md.digest();

    return new String( base64( sha1hash ) );
    }

    public byte[] base64(byte[] bytes) throws Exception
    {
    ByteArrayOutputStream out_bytes = new ByteArrayOutputStream();
    OutputStream out = new Base64.OutputStream(out_bytes); //Using http://iharder.net/base64
    out.write(bytes);
    out.close();
    return out_bytes.toByteArray();
    }

    private String convertToHex(byte[] data) { 
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) { 
        int halfbyte = (data[i] >>> 4) & 0x0F;
        int two_halfs = 0;
        do { 
        if ((0 <= halfbyte) && (halfbyte <= 9)) 
            buf.append((char) ('0' + halfbyte));
        else 
            buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
        } while(two_halfs++ < 1);
    } 
    return buf.toString();
    } 
}
