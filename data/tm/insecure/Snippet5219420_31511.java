package com.imap;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class DummyTrustManager implements X509TrustManager {

  public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException {

      // everything is trusted

  }

  public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException {

      // everything is trusted

  }

  public X509Certificate[] getAcceptedIssuers() {

      return new X509Certificate[0];

  }

} 
