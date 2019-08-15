import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.security.KeyStore;

public class SSLSocketTest 
{
    private SSLSocket sslSocket = null;
    private SSLSocketFactory sslSocketFactory = null;
    private String ipAddress = "192.168.100.99";
    private int port = 9999;

    DataOutputStream dataOS = null;
    DataInputStream dataIS = null;

    private boolean handshakeSuccessful = false;

    public static void main(String[] args) 
    {
        SSLSocketTest sslSocketTest = new SSLSocketTest();
        sslSocketTest.sslSocketConnect();
    }

    SSLSocketTest()
    {
        System.setProperty("javax.net.debug", "all");

        try{
            File certFile = new File("cacerts");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] certPassword = "changeit".toCharArray();
            InputStream fileIS = new FileInputStream(certFile);
            keyStore.load(fileIS, certPassword);
            fileIS.close();

            SSLContext sslContext = SSLContext.getInstance("TLSv1");

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            X509TrustManager defaultTrustManager = (X509TrustManager)trustManagerFactory.getTrustManagers()[0];

            sslContext.init(null, new TrustManager[] {defaultTrustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sslSocketConnect()
    {
        try{
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(ipAddress, port);

            dataOS = new DataOutputStream(sslSocket.getOutputStream());
            dataIS = new DataInputStream(sslSocket.getInputStream());

            sslSocket.setSoTimeout(15000);

            //Handshake
            sslSocket.addHandshakeCompletedListener(new MyHandshakeListener());
            sslSocket.startHandshake();
            while(!handshakeSuccessful)
            {
                Thread.sleep(100);
            }

            //Sending commands
            byte[] firstCommand = new byte[]{(byte)0x01, (byte)0x03, (byte)0x03};

            String[] firstCommandResponse = processCommand(firstCommand);

            byte[] secondCommand = new byte[]{(byte)0x01, (byte)0x48, (byte)0x65, (byte)0x6C, (byte)0x6C, (byte)0x6F};

            String[] secondCommandResponse = processCommand(secondCommand);

            disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        try{
            byte[] endConnection = new byte[]{(byte)0x01, (byte)0x01, (byte)0x02, (byte)0x03};

            processCommand(endConnection);

            dataOS.close();
            dataIS.close();
            sslSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] processCommand(byte[] command)
    {
        String[] returnResponse = null;

        byte[] commandResponse = new byte[120];
        byte[] trimCommandResponse;

        try{
            int commandResponseLength = -1;
            int errorCount = 0;

            while(commandResponseLength == -1)
            {   
                StringBuilder cmdStr = new StringBuilder();
                cmdStr.append("Sending: ");
                for(int i=0; i<command.length; i++)
                {
                    cmdStr.append(fixHexStringData(Integer.toHexString(command[i])) + " ");
                }
                System.out.println(cmdStr.toString());

                dataOS.write(command, 0, command.length);
                dataOS.flush();

                commandResponseLength = dataIS.read(commandResponse);

                errorCount++;
                if(errorCount == 3)
                {
                    throw new Exception();
                }
            }

            returnResponse = new String[commandResponseLength];
            trimCommandResponse = new byte[commandResponseLength];

            //Getting Reponse Data
            for(int i=0; i<commandResponseLength; i++)
            {
                returnResponse[i] = fixHexStringData(Integer.toHexString(commandResponse[i]));
                trimCommandResponse[i] = commandResponse[i];
            }

            StringBuilder rcvStr = new StringBuilder();             
            rcvStr.append("Receive: ");
            for(int i=0; i<returnResponse.length; i++)
            {
                rcvStr.append(returnResponse[i] + " ");
            }
            System.out.println(rcvStr.toString());

        }catch(Exception e){
            e.printStackTrace();
        }

        return returnResponse;
    }

    private String fixHexStringData(String dataByte)
    {
        if(dataByte.length() < 2)
        {       
            dataByte = "0" + dataByte;
        }
        else if(dataByte.length() > 2)
        {
            dataByte = dataByte.substring(dataByte.length()-2);
        }
        return dataByte;
    }

    class MyHandshakeListener implements HandshakeCompletedListener 
    {
          public void handshakeCompleted(HandshakeCompletedEvent e)
          {
            System.out.println("Handshake succesful!");

            handshakeSuccessful = true;
          }
    }
}
