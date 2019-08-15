package com.my.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class signinthread extends Thread
{
    Context context;
    InputStream keyin;
    public signinthread(InputStream frommain)
    {
        keyin=frommain;
        System.out.println("keyin loaded");
    }
    private TextView mSignInStatus;

    private static final String TAG  = "SignInActivity";
    private BufferedWriter out = null;
    private BufferedReader in = null;
    int port = 8080;
    String ip_address = "192.my.public.ip";
    private SSLSocket socket = null;
    private char keystorepass[] = "My Password".toCharArray();
    private char keypassword[] = "My Password"".toCharArray();
    public void run()
    {
        try
        {
            KeyStore ks = KeyStore.getInstance("BKS");
            //keyin = this.getResources().openRawResource(R.raw.bkskey);
            ks.load(keyin,keystorepass);
            SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            socket = (SSLSocket)socketFactory.createSocket(new Socket(ip_address,port), ip_address, port, false);
            socket.startHandshake();
            printServerCertificate(socket);
            printSocketInfo(socket);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msgToServer="login";
            chat(msgToServer);
        } catch (UnknownHostException e) {
            Log.i(TAG,"Unknown host");
        } catch  (IOException e) {
            Log.i(TAG,"No I/O");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (KeyStoreException e) {
            Log.i(TAG,"Keystore ks error");
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG,"No such algorithm for ks.load");
            e.printStackTrace();
        } catch (CertificateException e) {
            Log.i(TAG,"certificate missing");
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            Log.i(TAG,"unrecoverableKeyException");
            e.printStackTrace();
        } catch (KeyManagementException e) {
            Log.i(TAG,"key management exception");
            e.printStackTrace();
        }
    }
    private void printServerCertificate(SSLSocket socket) {
        try
        {
            java.security.cert.Certificate[] serverCerts = socket.getSession().getPeerCertificates();
            for (int i = 0; i < serverCerts.length; i++) {
                java.security.cert.Certificate myCert = serverCerts[i];
                Log.i(TAG,"====Certificate:" + (i+1) + "====");
                Log.i(TAG,"-Public Key-\n" + myCert.getPublicKey());
                Log.i(TAG,"-Certificate Type-\n " + myCert.getType());
                System.out.println();
            }
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG,"Could not verify peer");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    private void printSocketInfo(SSLSocket s) {
        Log.i(TAG,"Socket class: "+s.getClass());
        Log.i(TAG,"   Remote address = "+s.getInetAddress().toString());
        Log.i(TAG,"   Remote port = "+s.getPort());
        Log.i(TAG,"   Local socket address = "+s.getLocalSocketAddress().toString());
        Log.i(TAG,"   Local address = "+s.getLocalAddress().toString());
        Log.i(TAG,"   Local port = "+s.getLocalPort());
        Log.i(TAG,"   Need client authentication = "+s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        Log.i(TAG,"   Cipher suite = "+ss.getCipherSuite());
        Log.i(TAG,"   Protocol = "+ss.getProtocol());
    }
    public void chat(String temp)
    {
        String message = temp;
        String line = "";
        // send id of the device to match with the image
        try {
            out.write(message+"\n");
            out.flush();
        } catch (IOException e2) {
            Log.i(TAG,"Read failed");
            System.exit(1);
        }
        // receive a ready command from the server
        try {
            line = in.readLine();
            mSignInStatus.setText("SERVER SAID: "+line);
            System.out.println("from Server:"+line);
            //Log.i(TAG,line);
        } catch (IOException e1) {
            Log.i(TAG,"Read failed");
            System.exit(1);
        }
    }
}
