I made a custom socketFactory class called SSLSocketFactoryEx.java

import com.android.org.bouncycastle.util.encoders.Base64;
import com.pos.hsm.PKCS11Wrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Principal;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
import Decoder.BASE64Decoder;

import android.content.Context;
import android.content.res.AssetManager;
//import org.apache.commons.codec.binary.Base64;
import android.util.Log;
public class SSLSocketFactoryEx extends SSLSocketFactory {      
    private static final String[] TLS_SUPPORT_VERSION = {"TLSv1.1", "TLSv1.2"};//Android 5.1 default enable SSLv3

    SSLContext sslContext =SSLContext.getInstance("SSL");
    public SSLSocketFactoryEx(final Context context, KeyStore truststore)      
            throws NoSuchAlgorithmException, KeyManagementException,      
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager trustManager = new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                logwarn("alias =========getAcceptedIssuers===== ");
                return new X509Certificate[0];      
            }      

            @Override      
            public void checkClientTrusted(      
                    java.security.cert.X509Certificate[] chain, String authType)      
                    throws java.security.cert.CertificateException {
                logwarn("=====================checkServerTrusted===================");
                if(chain != null)logwarn("length = " + chain.length);
                try {
                    CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = null;
                    X509Certificate ca;
                    ByteArrayInputStream bais;

                    if(chain != null && chain.length > 0){
                        bais = new ByteArrayInputStream(chain[0].getEncoded());
                        cert = (X509Certificate) factory.generateCertificate(bais);
                        bais.close();
                    } else {
                        throw new CertificateException("There is no certificate");
                    }
                    byte[] trust = RSASignature.readFileByBytes(context, "trust01.crt");
                    logwarn("trust cert = " + new String(trust));
                    bais = new ByteArrayInputStream(trust);
                    CertificateFactory myCertificateFactory;
                    ca = (X509Certificate)factory.generateCertificate(bais);
                    bais.close();
                    cert.verify(ca.getPublicKey());
                }catch (Exception e){
                    logwarn("TrustManager checkServerTrusted failed!");
                    e.printStackTrace();
                    throw new CertificateException(e);
                }
            }

            @Override      
            public void checkServerTrusted(      
                    java.security.cert.X509Certificate[] chain, String authType)      
                    throws java.security.cert.CertificateException {      
                logwarn("=====================checkServerTrusted===================");
                if(chain != null)logwarn("length = " + chain.length);
                try {
                    CertificateFactory factory = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = null;
                    X509Certificate ca;
                    ByteArrayInputStream bais;
                    if(chain != null && chain.length > 0){
                        bais = new ByteArrayInputStream(chain[0].getEncoded());
                        cert = (X509Certificate) factory.generateCertificate(bais);
                        bais.close();
                    } else {
                        throw new CertificateException("There is no certificate");
                    }
                    byte[] trust =  RSASignature.readFileByBytes(context, "trust01.crt");
                    logwarn("trust cert = " + new String(trust));
                    bais = new ByteArrayInputStream(trust);
                    CertificateFactory myCertificateFactory;
                    ca = (X509Certificate)factory.generateCertificate(bais);
                    bais.close();
                    cert.verify(ca.getPublicKey());
                }catch (Exception e){
                    logwarn("TrustManager checkServerTrusted failed!");
                    e.printStackTrace();
                    throw new CertificateException(e);
                }
            }      
        };
        X509KeyManager keyManager = new X509KeyManager() {
            @Override
            public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {

                logwarn("=====================chooseClientAlias===================");
                if(keyType != null && keyType.length > 0){
                    for(int i = 0; i < keyType.length; i++){
                        logwarn("keyType["+i+"]=" + keyType[i]);
                    }
                }
                if(issuers != null && issuers.length > 0){
                    for(int i = 0; i < issuers.length; i++){
                        logwarn("issuers["+i+"]=" + issuers[i].getName());
                    }
                }
                return "pk2048";
            }

            @Override
            public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
                logwarn("=====================chooseServerAlias===================");
                logwarn("keyType================" + keyType);
                if(issuers != null && issuers.length > 0){
                    for(int i = 0; i < issuers.length; i++){
                        logwarn("issuers["+i+"]=" + issuers[i].getName());
                    }
                }
                return "trust01";
            }

            @Override
            public X509Certificate[] getCertificateChain(String alias) {
                logwarn("=====================getCertificateChain===================");
                logwarn("alias ============== " + alias);
                byte[] publickey = RSASignature.readFileByBytes(context, "client2048.crt");
                InputStream ceris = new ByteArrayInputStream(publickey);
                CertificateFactory myCertificateFactory;
                try {
                    myCertificateFactory = CertificateFactory.getInstance("X.509");
                    X509Certificate cer = (X509Certificate)myCertificateFactory.generateCertificate(ceris);
                    ceris.close();
                    return new X509Certificate[]{cer};
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return new X509Certificate[0];
            }

            @Override
            public String[] getClientAliases(String keyType, Principal[] issuers) {
                logwarn("=====================getClientAliases===================");
                logwarn("keyType================" + keyType);
                if(issuers != null && issuers.length > 0){
                    for(int i = 0; i < issuers.length; i++){
                        logwarn("issuers["+i+"]=" + issuers[i].getName());
                    }
                }
                return new String[0];
            }

            @Override
            public String[] getServerAliases(String keyType, Principal[] issuers) {
                logwarn("=====================getServerAliases===================");
                logwarn("keyType================" + keyType);
                if(issuers != null && issuers.length > 0){
                    for(int i = 0; i < issuers.length; i++){
                        logwarn("issuers["+i+"]=" + issuers[i].getName());
                    }
                }
                return new String[0];
            }

            @Override
            public PrivateKey getPrivateKey(String alias) {
                logwarn("=====================getPrivateKey===================");
                logwarn("alias ============== " + alias);
                byte[] keyData = RSASignature.readFileByBytes(context, alias + ".crt");
                String keyString = new String(keyData);
                keyString = keyString.replaceAll("\n", "");
                Pattern pattern =Pattern.compile("-----BEGIN RSA PRIVATE KEY-----(.*?)-----END RSA PRIVATE KEY-----");
                Matcher matcher=pattern.matcher(keyString);
                while (matcher.find()) {
                    keyData = matcher.group(1).getBytes();
                }
                try {
                    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(keyData));//PKCS8EncodedKeySpec(Base64.decodeBase64(keyData));
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA","BC");
                    PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
                    return privateK;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        //sslContext.init(null, new TrustManager[] { tm }, null);
        sslContext.init(new KeyManager[]{keyManager}, new TrustManager[]{trustManager}, null);

    }      

    @Override      
    public Socket createSocket(Socket socket, String host, int port,      
            boolean autoClose) throws IOException, UnknownHostException { 
        //injectHostname(socket, host);
        SSLSocket S = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        String[] enProtocols = S.getEnabledProtocols();
        for (String cipherSuite : enProtocols) {
            logwarn("enProtocols ====c========== " + cipherSuite);
          }
        String[] Protocols = S.getSupportedProtocols();
        for (String cipherSuite : Protocols) {
            logwarn("Protocols ====c========== " + cipherSuite);
          }
        S.setEnabledProtocols(TLS_SUPPORT_VERSION);
         enProtocols = S.getEnabledProtocols();
        for (String cipherSuite : enProtocols) {
            logwarn("enProtocols ====en========== " + cipherSuite);
          }
        Protocols = S.getSupportedProtocols();
        for (String cipherSuite : Protocols) {
            logwarn("Protocols ====add========== " + cipherSuite);
          }
        String[] original =S.getEnabledCipherSuites();
        for (String cipherSuite : original) {
            logwarn("cipherSuite ====c========== " + cipherSuite);
          }
        return sslContext.getSocketFactory().createSocket(socket, host, port,      
                autoClose);      
    }      
    private void injectHostname(Socket socket, String host) {
        try {
            Field field = InetAddress.class.getDeclaredField("hostName");
            field.setAccessible(true);
            field.set(socket.getInetAddress(), host);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
    @Override      
    public Socket createSocket() throws IOException {
        SSLSocket S = (SSLSocket) sslContext.getSocketFactory().createSocket();
        S.setEnabledProtocols(TLS_SUPPORT_VERSION);
        String[] original =S.getEnabledCipherSuites();
        for (String cipherSuite : original) {
            logwarn("cipherSuite ============== " + cipherSuite);
          }
        return sslContext.getSocketFactory().createSocket();      
    }
    void logwarn(String msg) {
        Log.d("AndroidSSLSocketFactory", msg);
    }
}  
