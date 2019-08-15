import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public void request(final BaseNetData baseNetData, final MyNetCallbacks myNetCallbacks) {
        Log.d(TAG, " Getting url : " + baseNetData.getUrl());
        Log.d(TAG, " Method : " + baseNetData.getMethod());

    final StringRequest strReq = new StringRequest(baseNetData.getMethod(), baseNetData.getUrl(), new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(TAG, "onResponse : " + response);
            //My Code
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "inside onErrorResponse : " + error);
            //My Code
        }
    }) {
        @Override
        public byte[] getBody() throws AuthFailureError {
            Log.d(TAG, "Body : " + new String(baseNetData.getBody()));
            return baseNetData.getBody();
        }

        @Override
        public String getBodyContentType() {
            return baseNetData.getContentType();
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            Log.d(TAG, "  inside parseNetworkResponse, responseCode : " + response.statusCode);
            // My Code
            return super.parseNetworkResponse(response);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Util.displayMap(TAG, baseNetData.getHeaders());
            return baseNetData.getHeaders();
        }
    };
    BaseApplication.getInstance().addToRequestQueue(strReq, hurlStack, "");
}


HurlStack hurlStack = new HurlStack() {
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
        try {
            httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
            httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpsURLConnection;
    }
};

private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //return true;
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("myhost.com", session);
        }
    };
}

private SSLSocketFactory getSSLSocketFactory()
        throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, java.security.cert.CertificateException {
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = BaseApplication.getInstance().getResources().openRawResource(R.raw.ca_certificate); // this cert file stored in \app\src\main\res\raw folder path

    Certificate ca = cf.generateCertificate(caInput);
    caInput.close();

    KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, wrappedTrustManagers, null);

    return sslContext.getSocketFactory();
}

private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
    final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
    return new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return originalTrustManager.getAcceptedIssuers();
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkClientTrusted(certs, authType);
                    } catch (CertificateException ignored) {
                    }
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    try {
                        originalTrustManager.checkServerTrusted(certs, authType);
                    } catch (CertificateException ignored) {
                    }
                }
            }
    };
}
