    /* Server SSL */


import javax.net.ssl.*;
import java.io.*;
import java.net.InetAddress;
import java.security.KeyStore;
//import java.security.cert.X509Certificate;
import java.util.Date;
import javax.security.cert.X509Certificate;

class server {

    ObjectOutputStream out;
    ObjectInputStream in;

    public static void main(String[] args) {    
        new server().run();
    }

    void run() {

    Date date = new Date();


        try {

            // Security test (to avoid starting from the command line)c

            char[] passphrase = "password".toCharArray();

           KeyStore keystore = KeyStore.getInstance("JKS");


           keystore.load(new FileInputStream("/home/ME/CERT/keystore"), passphrase);


           KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
           kmf.init(keystore, passphrase);


           SSLContext context = SSLContext.getInstance("TLS");
           KeyManager[] keyManagers = kmf.getKeyManagers();

           context.init(keyManagers, null, null);


            // -- End of security test

        // 1. Create a server Socket
            SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslserversocket = (SSLServerSocket) sslserversocketfactory.createServerSocket(9999);

         // TBH im not sure what the following code does, but I need it?

                    String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
                    sslserversocket.setEnabledCipherSuites(enabledCipherSuites);



        // 2. Wait for connection.
        System.out.println("Waiting for connection. ");
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

           SSLSession session = sslsocket.getSession();
           X509Certificate cert = (X509Certificate)session.getPeerCertificateChain()[0];
           String subject = cert.getSubjectDN().getName();
           System.out.println(subject);


        // 2.1 Prints information about client, this could be (very) valuable to save in a file.
        System.out.println("Connection received: ");
        System.out.println("   Hostname: " + sslsocket.getInetAddress().getHostName());
        sslsocket.getInetAddress();
        System.out.println("         IP: " + InetAddress.getLocalHost().getHostAddress());
        System.out.println("          @  " + date.toString());
        System.out.println();

        // 3. Create input and output streams.



        // for some reason i cant make the out/in-variable without using it as global?

        out = new ObjectOutputStream(sslsocket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(sslsocket.getInputStream());

        sendMessage("Connection successful");

        // 4. Client and server talks via the input and output streams

        String message;

        do {
        message = (String)in.readObject();
        System.out.println(" Client says:  " + message);
        if (message.equals("bye")) {
            sendMessage("bye");
        }

        }while(!message.equals("bye"));

        // 5. Closing connection

        in.close();
        out.close();
        sslsocket.close();
System.out.println("Server terminated. ");

        } catch (Exception exception) {
            exception.printStackTrace();

        }
    }
    void sendMessage(String msg) throws Exception
    {
    out.writeObject(msg);
    out.flush();
    System.out.println("  You(server) says: " + msg);
    }


}
