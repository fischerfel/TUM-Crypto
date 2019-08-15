public class SSLFactory extends SSLSocketFactory {
SSLContext sslContext = SSLContext.getInstance("TLS");

public SSLFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(truststore);

    X509TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    sslContext.init(null, new X509TrustManager[] { tm }, null);
}

public SSLFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
   super(null);
   sslContext = context;
}

@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
    return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
}

@Override
public Socket createSocket() throws IOException {
    return sslContext.getSocketFactory().createSocket();
}

public static HttpClient sslClient(HttpClient client) {
    try {
        X509TrustManager tm = new X509TrustManager() { 
            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", ssf, 443));
        return new DefaultHttpClient(ccm, client.getParams());
    } catch (Exception ex) {
        return null;
    }
}
 }
