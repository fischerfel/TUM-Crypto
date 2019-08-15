import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.awt.*;

import javax.swing.*;

import org.apache.commons.codec.binary.Base64;

public class Server extends JFrame
{
private JTextArea textArea = new JTextArea();
private Scanner fromClient;
private BufferedOutputStream toClient;
private Socket socket;
private static final int TEXT = 1, BINARY = 2, CLOSE = 8, PING = 9, PONG = 10;

public static void main(String[] args)
{
    new Server();
}

public Server()
{
    setLayout(new BorderLayout());
    add(new JScrollPane(textArea), BorderLayout.CENTER);

    setTitle("Server");
    setSize(700, 400);
    setLocation(0, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    try 
    {
        //create server socket
        ServerSocket serverSocket = new ServerSocket(8181);
        textArea.append( "Starting Server...\n" );
        textArea.append("Server started at " + new Date() + '\n');

        //Wait for connection
        socket = serverSocket.accept();

        //create input output
        fromClient = new Scanner(socket.getInputStream());
        toClient = new BufferedOutputStream(socket.getOutputStream());

        this.handle_handshake(fromClient);
        textArea.append("\n\nsending text....\n");
        //this.brodcast("pop");
        this.send_message("test");

        int i = 1; 
        while (true)
        {
            String s = fromClient.hasNext() ? fromClient.nextLine() : "";
            textArea.append("Client: " + s + "\n");
            if( i == 0 ) break;
        }

        socket.close();
        serverSocket.close();
    }
    catch (IOException ex)
    {
        System.err.println(ex);
    }
}

private boolean handle_handshake( Scanner scanner ) 
{
    String line;
    String hash_str = "";
    String key = "";


    int counter = 0;
    while ( scanner.hasNextLine() && (line = scanner.nextLine() ) != null ) 
    {
        String[] tokens = line.split( ": " );


        switch ( tokens[0] ) 
        {

            case "Sec-WebSocket-Key":
                key = tokens[1].trim();

                textArea.append( "SocketKey" );
                hash_str = this.get_return_hash( tokens[1] );
                break;

            case "Sec-WebSocket-Version":
                textArea.append( "WebSock-Ver: " + tokens[1] + "\n" );
                break;

            default:
                textArea.append( tokens[0] + ": " + tokens[tokens.length-1] + "\n" );
                break;
        }

        ++counter;

        if ( counter > 12 ) 
        {
            textArea.append( "Handshake" );
            String msg = "HTTP/1.1 101 Switching Protocols\r\n";
            msg += "Upgrade: websocket\r\n";
            msg += "Connection: Upgrade\r\n";
            msg += "Sec-WebSocket-Accept: " + hash_str + "\r\n";
            msg += "\r\n\r\n";

            try
            {
                toClient.write( msg.getBytes( "UTF-8" ) );
                textArea.append( "Server > Client: " + msg );
                //toClient.flush();
            }
            catch (UnsupportedEncodingException e) 
            {
                e.printStackTrace();
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }

            textArea.append( "\n\nClosing handshake\n\n" );

            return true;
        }
    }

    return false;
}

public void brodcast(String mess) throws IOException
{
    byte[] rawData = mess.getBytes();

    int frameCount  = 0;
    byte[] frame = new byte[10];

    frame[0] = TEXT;

    if(rawData.length <= 125){
        frame[1] = (byte) rawData.length;
        frameCount = 2;
    }else if(rawData.length >= 126 && rawData.length <= 65535){
        frame[1] = (byte) 126;
        byte len = (byte) rawData.length;
        frame[2] = (byte)((len >> 8 ) & (byte)255);
        frame[3] = (byte)(len & (byte)255); 
        frameCount = 4;
    }else{
        frame[1] = (byte) 127;
        byte len = (byte) rawData.length;
        frame[2] = (byte)((len >> 56 ) & (byte)255);
        frame[3] = (byte)((len >> 48 ) & (byte)255);
        frame[4] = (byte)((len >> 40 ) & (byte)255);
        frame[5] = (byte)((len >> 32 ) & (byte)255);
        frame[6] = (byte)((len >> 24 ) & (byte)255);
        frame[7] = (byte)((len >> 16 ) & (byte)255);
        frame[8] = (byte)((len >> 8 ) & (byte)255);
        frame[9] = (byte)(len & (byte)255);
        frameCount = 10;
    }

    int bLength = frameCount + rawData.length;

    byte[] reply = new byte[bLength];

    int bLim = 0;
    for(int i=0; i<frameCount;i++){
        reply[bLim] = frame[i];
        bLim++;
    }
    for(int i=0; i<rawData.length;i++){
        reply[bLim] = rawData[i];
        bLim++;
    }


    toClient.write(reply);
    toClient.flush();

    textArea.append("\n\n\nEnd of brodcasting: " + mess + "\n\n\n");
}

private void send_message( String msg ) 
{   
    try 
    {
        textArea.append( "Sending Message: " + msg + "\n" );
        byte[] textBytes = msg.getBytes( "UTF-8" );
        //ByteArrayOutputStream bao = new ByteArrayOutputStream();

        byte code = TEXT;

        //opcode
        toClient.write( code );

        //length
        toClient.write( (byte) textBytes.length );

        //data
        toClient.write( textBytes );

        //fin end line
        toClient.write( code );
        toClient.flush();

    }
    catch ( IOException e )
    {
        System.out.printf( "IOE: %s", e.getMessage() ); 
    }

}

private String get_return_hash( String key ) 
{
    textArea.append( "|| " + key + " ||\n"  );
    String protocol_str = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    byte[] sha1_bytes = sha1( key + protocol_str );

    String final_hash = new String(Base64.encodeBase64( sha1_bytes ));

    return final_hash;    
}

private byte[] sha1(String input) 
{
    try
    {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());

        return result;
    } 
    catch ( NoSuchAlgorithmException e ) 
    {
        return null;
    }
}

}
