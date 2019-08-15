package sslTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TestMain { 
    public class TestModuleReceive {
        int port = 4445;
        SSLServerSocket ss;
        SSLSocket socket;

        TestModuleReceive() {
            initServerSocket();
            receiveConnection();
        }

        private void initServerSocket() {
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(new FileInputStream("/home/uName/.keystore"), ("password").toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(ks, ("password").toCharArray());
                SSLContext sslcontext = SSLContext.getInstance("SSLv3");
                sslcontext.init(kmf.getKeyManagers(), null, null);
                ServerSocketFactory ssf =
                    sslcontext.getServerSocketFactory();
                ss = (SSLServerSocket)
                    ssf.createServerSocket();
                ss.setReceiveBufferSize(8200);
                ss.bind(new InetSocketAddress(port));
            } 
            catch (KeyStoreException e) { e.printStackTrace(); } 
            catch (NoSuchAlgorithmException e) { e.printStackTrace(); } 
            catch (CertificateException e) { e.printStackTrace(); } 
            catch (FileNotFoundException e) { e.printStackTrace(); } 
            catch (IOException e) { e.printStackTrace(); } 
            catch (UnrecoverableKeyException e) { e.printStackTrace(); } 
            catch (KeyManagementException e) { e.printStackTrace(); }
        }

        public void receiveConnection() {
            new Thread() { public void run() {
                try {
                    socket = (SSLSocket) ss.accept();
                    receiveData();
                } 
                catch (IOException e) { e.printStackTrace(); } 
            }}.start();
        }

        public void receiveData() {
            try {
                System.out.println(socket.getInputStream().read());
            } 
            catch (IOException e) { e.printStackTrace(); } 
        }
    }

    public class TestModuleSend {

        int port = 4445;
        String address = "localhost";
        SSLSocketFactory factory;
        SSLSocket socket;

        TestModuleSend() {
            initFactory();
            addConnection();
            sendData();
        }

        private void initFactory() {
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                ks.load(new FileInputStream("/home/uName/.keystore"), ("password").toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                kmf.init(ks, ("password").toCharArray());
                SSLContext sslcontext = SSLContext.getInstance("SSLv3");
                sslcontext.init(kmf.getKeyManagers(), null, null);
                factory = sslcontext.getSocketFactory();
            } 
            catch (KeyStoreException e) { e.printStackTrace(); } 
            catch (NoSuchAlgorithmException e) { e.printStackTrace(); } 
            catch (CertificateException e) { e.printStackTrace(); } 
            catch (FileNotFoundException e) { e.printStackTrace(); } 
            catch (IOException e) { e.printStackTrace(); } 
            catch (UnrecoverableKeyException e) { e.printStackTrace(); } 
            catch (KeyManagementException e) { e.printStackTrace(); }
        }

        public void addConnection() {
            // Initialize socket
            try {
                socket = (SSLSocket) factory.createSocket(InetAddress.getByName(address), port);
            } 
            catch (UnknownHostException e1) { e1.printStackTrace(); } 
            catch (IOException e1) { e1.printStackTrace(); }
        }

        public void sendData() {
            // Send single byte
            try {
                socket.getOutputStream().write((byte)0x00);
            } 
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    TestMain() {
        TestModuleReceive receive = new TestModuleReceive();
        TestModuleSend send = new TestModuleSend();
    }

    public static void main(String[] args) {
        TestMain testMain = new TestMain();
    }
}
