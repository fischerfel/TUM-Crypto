   import java.net.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import javax.net.ssl.*;

public class ChatClient implements Runnable
{
    private SSLSocket socket           = null;
    private Thread thread              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client    = null;
    final String[] enabledCipherSuites = {"TLS_RSA_WITH_AES_256_CBC_SHA256"};
    final char[] passphrase = "123456".toCharArray();

    public ChatClient(String serverName, int serverPort)
    {
        System.out.println("Establishing connection to server...");
        try
        {
            SSLSocketFactory factory = null;
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

            KeyStore ks= KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("clientkeystore"), passphrase);
            kmf.init(ks, passphrase);


            KeyStore serverKey = KeyStore.getInstance("JKS");
            serverKey.load(new FileInputStream("serverkeystore"),passphrase);
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
            trustManager.init(serverKey);



            ctx.init(kmf.getKeyManagers(), trustManager.getTrustManagers(), null);
            factory = ctx.getSocketFactory();
            socket = (SSLSocket)factory.createSocket(serverName, serverPort);
            socket.setEnabledCipherSuites(enabledCipherSuites);
            start();
        }

        catch(UnknownHostException uhe)
        {
            // Host unkwnown
            System.out.println("Error establishing connection - host unknown: " + uhe.getMessage());
        }
        catch(IOException ioexception)
        {
            // Other error establishing connection
            System.out.println("Error establishing connection - unexpected exception: " + ioexception.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while (thread != null)
        {
            try
            {
                // Sends message from console to server
                streamOut.writeUTF(console.readLine());
                streamOut.flush();
            }

            catch(IOException ioexception)
            {
                System.out.println("Error sending string to server: " + ioexception.getMessage());
                stop();
            }
        }
    }


    public void handle(String msg)
    {
        // Receives message from server
        if (msg.equals(".quit"))
        {
            // Leaving, quit command
            System.out.println("Exiting...Please press RETURN to exit ...");
            stop();
        }
        else
            // else, writes message received from server to console
            System.out.println(msg);
    }

    // Inits new client thread
    public void start() throws IOException
    {
        console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null)
        {
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    // Stops client thread
    public void stop()
    {
        if (thread != null)
        {
            thread.stop();
            thread = null;
        }
        try
        {
            if (console   != null)  console.close();
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }

        catch(IOException ioe)
        {
            System.out.println("Error closing thread..."); }
        client.close();
        client.stop();
    }


    public static void main(String args[])
    {
        ChatClient client = null;
        if (args.length != 2)
            // Displays correct usage syntax on stdout
            System.out.println("Usage: java ChatClient host port");
        else
            // Calls new client
            client = new ChatClient(args[0], Integer.parseInt(args[1]));
    }

}

class ChatClientThread extends Thread
{
    private SSLSocket        socket   = null;
    private ChatClient       client   = null;
    private DataInputStream  streamIn = null;

    public ChatClientThread(ChatClient _client, SSLSocket _socket)
    {
        client   = _client;
        socket   = _socket;
        open();
        start();
    }

    public void open()
    {
        try
        {
            streamIn  = new DataInputStream(socket.getInputStream());
        }
        catch(IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null) streamIn.close();
        }

        catch(IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    public void run()
    {
        while (true)
        {   try
        {
            client.handle(streamIn.readUTF());
        }
        catch(IOException ioe)
        {
            System.out.println("Listening error: " + ioe.getMessage());
            client.stop();
        }
        }
    }
}
