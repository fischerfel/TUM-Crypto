package serverapplicationssl;


import java.io.*;
import java.security.KeyStore;
import java.security.Security;
import java.security.PrivilegedActionException;

import javax.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

import java.io.*;

public class ServerApplicationSSL {

public static void main(String[] args) {
boolean debug = true;

System.out.println("Waiting For Connection");

int intSSLport = 4447;

{
    Security.addProvider(new Provider());

}
if (debug) {
    System.setProperty("javax.net.debug", "all");
}
FileWriter file = null;
try {
    file = new FileWriter("C:\\SSLCERT\\Javalog.txt");

} catch (Exception ee) {
    //message = ee.getMessage();

}

try {

    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(new FileInputStream("C:\\SSLCERT\\OntechServerKS"), "server".toCharArray());
    file.write("Incoming Connection\r\n");

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
            .getDefaultAlgorithm());
    kmf.init(keystore, "server".toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), null, null);

    SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) context.getServerSocketFactory();
    SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(intSSLport);
    sslServerSocket.setEnabledCipherSuites(sslServerSocket.getSupportedCipherSuites());
    sslServerSocket.setNeedClientAuth(true);
    SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
    //SSLServerSocket server_socket = (SSLServerSocket) sslServerSocket;

    sslSocket.startHandshake();

 // Start the session
    System.out.println("Connection Accepted");
    file.write("Connection Accepted\r\n");

    while (true) {
        PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);

        String inputLine;

        //while ((inputLine = in.readLine()) != null) {
        out.println("Hello Client....Welcome");
        System.out.println("Hello Client....Welcome");
        //}

        out.close();
        //in.close();
        sslSocket.close();
        sslServerSocket.close();
        file.flush();
        file.close();
    }

} catch (Exception exp) {
    try {
        System.out.println(exp.getMessage() + "\r\n");
        exp.printStackTrace();
        file.write(exp.getMessage() + "\r\n");
        file.flush();
        file.close();
    } catch (Exception eee) {
        //message = eee.getMessage();
    }

}

}

}
