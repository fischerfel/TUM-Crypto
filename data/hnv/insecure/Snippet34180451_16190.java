package com.example.ssltest;    
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
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.security.cert.Certificate;   
import org.apache.http.conn.ssl.SSLSocketFactory;
 import android.app.Activity;
 import android.content.Context;
 import android.os.Bundle;
 import android.os.StrictMode;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;
public class CopyOfMainActivity extends Activity {
/** Called when the activity is first created. */
private EditText mText;
private Button mSend;
private TextView mResponse;
private EditText mIPaddress;
private EditText mPort;
// port to use
//private String ip_address="103.9.105.15";
private String ip_address="103.9.105.15";
private int port = 443;
private SSLSocket socket = null;
private BufferedWriter out = null;
private BufferedReader in = null;
private final String TAG = "TAG";
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mText = (EditText) findViewById(R.id.editext);
    mSend = (Button) findViewById(R.id.send_button);
    mResponse = (TextView) findViewById(R.id.server_response);       
    mSend.setClickable(true);       
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
    mSend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                try{
  KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                    ks.load(null, null);
  SSLSocketFactory socketFactory = new MySSLSocketFactory(ks);
                    socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                    socket = (SSLSocket) 
                    socketFactory.createSocket(new Socket(ip_address,port), ip_address, port, false);
                    socket.startHandshake();
                    socket.setSoTimeout(10000);
                    printServerCertificate(socket);
                    printSocketInfo(socket);
                    BufferedWriter w = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader r = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    w.write("GET / HTTP/1.0");
                    w.newLine();
                    System.out.println("send..");
                    w.flush();
                    String m ;
                    while ((m = r.readLine()) != null) {
                    System.out.println("status received: " + m);
                    }
                    System.out.println("after while");
                    w.close();
                    r.close();
                    socket.close();
                } catch (UnknownHostException e) {
                    Toast.makeText(v.getContext(), "Unknown host", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"Unknown host");
                    e.printStackTrace();
                    //System.exit(1);
                } catch  (IOException e) {
                    Toast.makeText(v.getContext(), "No I/O", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"No I/O");
                    e.printStackTrace();
                    //System.exit(1);
                } catch (KeyStoreException e) {
                    Toast.makeText(v.getContext(), "Keystore ks error", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"Keystore ks error");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (NoSuchAlgorithmException e) {
                    Toast.makeText(v.getContext(), "No such algorithm for ks.load", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"No such algorithm for ks.load");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (CertificateException e) {
                    Toast.makeText(v.getContext(), "certificate missing", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"certificate missing");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (UnrecoverableKeyException e) {
                    Toast.makeText(v.getContext(), "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"unrecoverableKeyException");
                    e.printStackTrace();
                    //System.exit(-1);
                } catch (KeyManagementException e) {
                    Toast.makeText(v.getContext(), "KeyManagementException", Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"key management exception");
                    e.printStackTrace();
                    //System.exit(-1);
                }
            }
    });
 }
private void printServerCertificate(SSLSocket socket) {
    try {
        Certificate[] serverCerts =
            socket.getSession().getPeerCertificates();
        for (int i = 0; i < serverCerts.length; i++) {
            Certificate myCert = serverCerts[i];
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
    Log.i(TAG,"   Remote address = "
            +s.getInetAddress().toString());
    Log.i(TAG,"   Remote port = "+s.getPort());
    Log.i(TAG,"   Local socket address = "
            +s.getLocalSocketAddress().toString());
    Log.i(TAG,"   Local address = "
            +s.getLocalAddress().toString());
    Log.i(TAG,"   Local port = "+s.getLocalPort());
    Log.i(TAG,"   Need client authentication = "
            +s.getNeedClientAuth());
    SSLSession ss = s.getSession();
    Log.i(TAG,"   Cipher suite = "+ss.getCipherSuite());
    Log.i(TAG,"   Protocol = "+ss.getProtocol());
}
public void chat(String temp){
    String message = temp;
    String line = "";// 
    // send id of the device to match with the image
    try {
        out.write("GET / HTTP/1.0");
        out.newLine();
        out.flush();
    } catch (IOException e2) {
        Log.i(TAG,"Read failed");
        //System.exit(1);
        e2.printStackTrace();
    }
    // receive a ready command from the server
    try {
        line = in.readLine();
        mResponse.setText("SERVER SAID: "+line);
        //Log.i(TAG,line);
    } catch (IOException e1) {
        Log.i(TAG,"Read failed");
        e1.printStackTrace();
       // System.exit(1);
    }
}
}
