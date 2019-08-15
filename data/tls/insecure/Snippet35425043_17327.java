    SSLContext sslContext = null;
                    try{
                        sslContext = SSLContext.getInstance("SSL");
                        ServerTrustManager serverTrustManager = new ServerTrustManager();
                        sslContext.init(null, new TrustManager[]{serverTrustManager}, null);

                    }catch(Exception e){
                        logger.error("Error while getting SSL context", e);
                    }


=================================================


package com.common.restclient;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class ServerTrustManager implements X509TrustManager{

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        // TODO Auto-generated method stub
        X509Certificate cert=null;


        try (InputStream inStream = new FileInputStream("SSLCertificate/salientrisk.crt")) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate)cf.generateCertificate(inStream);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cert.checkValidity();
        cert.getIssuerUniqueID();
        cert.getSubjectDN();



    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}
