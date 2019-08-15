import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import otherpackege.OtherClass;

import android.content.Context;
import android.util.Log;

public class SSLClient 
{
    static SSLContext ssl_ctx;

    public SSLClient(Context context)
    {
        try
        {
            // Setup truststore
            KeyStore trustStore = KeyStore.getInstance("BKS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            InputStream trustStoreStream = context.getResources().openRawResource(R.raw.mysrvtruststore);
            trustStore.load(trustStoreStream, "testtest".toCharArray());
            trustManagerFactory.init(trustStore);

            // Setup keystore
            KeyStore keyStore = KeyStore.getInstance("BKS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            InputStream keyStoreStream = context.getResources().openRawResource(R.raw.clientkeystore);
keyStore.load(keyStoreStream, "testtest".toCharArray());
            keyManagerFactory.init(keyStore, "testtest".toCharArray());

            Log.d("SSL", "Key " + keyStore.size());
            Log.d("SSL", "Trust " + trustStore.size());

            // Setup the SSL context to use the truststore and keystore
            ssl_ctx = SSLContext.getInstance("TLS");
            ssl_ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            Log.d("SSL", "keyManagerFactory " + keyManagerFactory.getKeyManagers().length);
            Log.d("SSL", "trustManagerFactory " + trustManagerFactory.getTrustManagers().length);
        }
        catch (NoSuchAlgorithmException nsae)
        {
            Log.d("SSL", nsae.getMessage());
        }
        catch (KeyStoreException kse)
        {
            Log.d("SSL", kse.getMessage());
        }
        catch (IOException ioe)
        {
            Log.d("SSL", ioe.getMessage());
        }
        catch (CertificateException ce)
        {
            Log.d("SSL", ce.getMessage());
        }
        catch (KeyManagementException kme)
        {
            Log.d("SSL", kme.getMessage());
        }
        catch(AccessControlException ace)
        {
            Log.d("SSL", ace.getMessage());
        }
        catch(UnrecoverableKeyException uke)
        {
            Log.d("SSL", uke.getMessage());
        }

        try
        {
            Handler handler = new Handler();
            handler.start();
        }
        catch (IOException ioException) 
        {
            ioException.printStackTrace();
        }
     }  
}

//class Handler implements Runnable 
class Handler extends Thread
{
    private SSLSocket socket;
    private BufferedReader input;
    static public PrintWriter output;

    private String serverUrl = "174.61.103.206";
    private String serverPort = "6000";

    Handler(SSLSocket socket) throws IOException
    {

    }
    Handler() throws IOException
    {

    }

    public void sendMessagameInfoge(String message)
    {
        Handler.output.println(message);
    }

    @Override
    public void run() 
    {
        String line;

        try 
        {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLClient.ssl_ctx.getSocketFactory();
            socket = (SSLSocket) socketFactory.createSocket(serverUrl, Integer.parseInt(serverPort));
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Handler.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Log.d("SSL", "Created the socket, input, and output!!");

            do
            {
                line = input.readLine();
                while (line == null)
                {
                    line = input.readLine();
                }

                // Parse the message and do something with it
                // Done in a different class
                OtherClass.parseMessageString(line);
            }
            while ( !line.equals("exit|") );
        }
        catch (IOException ioe)
        {
            System.out.println(ioe);
        }
        finally 
        {
            try 
            {
                input.close();
                output.close();
                socket.close();
            } 
            catch(IOException ioe) 
            {
            } 
            finally 
            {

            }
        }
    }
}
