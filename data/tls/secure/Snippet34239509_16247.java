package com.company;

import javax.net.ssl.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;

class MyClass implements Serializable
{
    private int i,j;

    public MyClass(int i, int j)
    {
        this.i = i;
        this.j = j;
    }

    public int getJ()
    {
        return j;
    }

    public void setJ(int j)
    {
        this.j = j;
    }

    public int getI()
    {
        return i;
    }

    public void setI(int i)
    {
        this.i = i;
    }
}
class SSLContextHelper
{
    static SSLContext createSSLContext(String path) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, CertificateException
    {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new FileInputStream(path),"DSL2137976".toCharArray());

        // Create key manager
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, "DSL2137976".toCharArray());
        KeyManager[] km = keyManagerFactory.getKeyManagers();

        // Create trust manager
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        TrustManager[] tm = trustManagerFactory.getTrustManagers();

        // Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(km,  tm, new SecureRandom());

        return sslContext;
    }
}
class ServerThread extends Thread
{
    ServerSocket server;
    Socket client;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean issecure;
    SSLContext sslContext;
    public ServerThread(int port, boolean issecure) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        this.issecure=issecure;
        client=null;
        if(issecure)
        {
            sslContext = SSLContextHelper.createSSLContext("/usr/lib/jvm/java-8-openjdk/jre/lib/security/ssltest");
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            server = sslServerSocketFactory.createServerSocket(port);
            server.setSoTimeout(200);
        }
        else
            server=new ServerSocket(port);
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                if(client==null)
                {
                    if (issecure)
                    {
                        SSLSocket clientssl = (SSLSocket) server.accept();
                        clientssl.setEnabledCipherSuites(clientssl.getSupportedCipherSuites());
                        clientssl.startHandshake();
                        client = clientssl;
                    }
                    else
                        client = server.accept();

                    in = new ObjectInputStream(client.getInputStream());
                    out = new ObjectOutputStream(client.getOutputStream());
                    client.setSoTimeout(200);
                }
                String[] req = in.readUTF().split("\n");
                out.writeUTF("hello I'm the server");
                out.flush();
                req = in.readUTF().split("\n");
                out.writeUTF("I mean I'll serve you");
                out.flush();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
public class Main
{
    public static void main(String... args) throws IOException, ClassNotFoundException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException
    {
        ServerThread serverThread=new ServerThread(14200, true);
        serverThread.setDaemon(true);
        serverThread.start();
        ServerThread mail=new ServerThread(14201, false);
        mail.setDaemon(true);
        mail.start();
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        SSLSocket client=(SSLSocket)SSLContextHelper.createSSLContext("/usr/lib/jvm/java-8-openjdk/jre/lib/security/ssltest").getSocketFactory().createSocket();
        client.connect(new InetSocketAddress("localhost",14200),5000);
        Socket mailclient = new Socket();
        mailclient.connect(new InetSocketAddress("localhost", 14201), 5000);
        client.startHandshake();
        client.setSoTimeout(5000);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(client.getInputStream());

        out.writeUTF("hello\nhow are you");
        out.flush();
        System.out.println(in.readUTF());
        out.writeUTF("what\nI didn't understand");
        out.flush();
        System.out.println(in.readUTF());
        int i=0;
        while (i<=1)
        {
            try
            {
                try
                {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                out.writeUTF("hello\nhow are you");
                out.flush();
                System.out.println(in.readUTF());
                out.writeUTF("what\nI didn't understand");
                out.flush();
                System.out.println(in.readUTF());
                i++;
            }
            catch (SocketTimeoutException ignored)
            {

            }
        }
    }
}
