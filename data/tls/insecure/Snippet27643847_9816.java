package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class SslReverseEchoer {
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

        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888);
        SSLSocket c = (SSLSocket) s.accept();
        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
                c.getOutputStream()));
        BufferedReader r = new BufferedReader(new InputStreamReader(
                c.getInputStream()));
        String m = "Welcome to SSL Reverse Echo Server."
                + " Please type in some words.";
        w.write(m, 0, m.length());
        w.newLine();
        w.flush();
        while ((m = r.readLine()) != null) {
            if (m.equals("."))
                break;
            char[] a = m.toCharArray();
            int n = a.length;
            for (int i = 0; i < n / 2; i++) {
                char t = a[i];
                a[i] = a[n - 1 - i];
                a[n - i - 1] = t;
            }
            w.write(a, 0, n);
            w.newLine();
            w.flush();
        }
        w.close();
        r.close();
        c.close();
        s.close();
    }
}
