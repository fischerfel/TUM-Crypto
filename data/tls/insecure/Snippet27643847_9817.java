package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SslSocketClient {
    public static void main(String[] args) throws Exception {
        String ksName = "keystore.jks";
        char ksPass[] = "".toCharArray();
        char ctPass[] = "".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ctPass);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;

        SSLSocketFactory f = (SSLSocketFactory) sc.getSocketFactory();
        SSLSocket c = (SSLSocket) f.createSocket("localhost", 8888);
        c.startHandshake();
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
                c.getOutputStream()));
        BufferedReader r = new BufferedReader(new InputStreamReader(
                c.getInputStream()));
        String m = null;
        while ((m = r.readLine()) != null) {
            out.println(m);
            m = in.readLine();
            w.write(m, 0, m.length());
            w.newLine();
            w.flush();
        }
        w.close();
        r.close();
        c.close();
    }
}
