        public class EasySSLSocketFactory implements SocketFactory, LayeredSocketFactory {

            private SSLContext sslcontext = null;

            private SSLContext createEasySSLContext() throws IOException {
                try {
                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
                    return context;
                } catch (Exception e) {
                    throw new IOException(e.getMessage());
                }
            }

            private SSLContext getSSLContext() throws IOException {
                if (this.sslcontext == null) {
                    this.sslcontext = createEasySSLContext();
                }
                return this.sslcontext;
            }

            /**
             * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket, java.lang.String, int,
             *      java.net.InetAddress, int, org.apache.http.params.HttpParams)
             */
            public Socket connectSocket(Socket sock, String host, int port, InetAddress localAddress, int localPort,
                    HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
                int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
                int soTimeout = HttpConnectionParams.getSoTimeout(params);
                InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
                SSLSocket sslsock = (SSLSocket) ((sock != null) ? sock : createSocket());

                if ((localAddress != null) || (localPort > 0)) {
                    // we need to bind explicitly
                    if (localPort < 0) {
                        localPort = 0; // indicates "any"
                    }
                    InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
                    sslsock.bind(isa);
                }

                sslsock.connect(remoteAddress, connTimeout);
                sslsock.setSoTimeout(soTimeout);
                return sslsock;

            }

            /**
             * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
             */
            public Socket createSocket() throws IOException {
                return getSSLContext().getSocketFactory().createSocket();
            }

            /**
             * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
             */
            public boolean isSecure(Socket socket) throws IllegalArgumentException {
                return true;
            }

            /**
             * @see org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net.Socket, java.lang.String, int,
             *      boolean)
             */
            public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                    UnknownHostException {
                return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
            }

            // -------------------------------------------------------------------
            // javadoc in org.apache.http.conn.scheme.SocketFactory says :
            // Both Object.equals() and Object.hashCode() must be overridden
            // for the correct operation of some connection managers
            // -------------------------------------------------------------------

            public boolean equals(Object obj) {
                return ((obj != null) && obj.getClass().equals(EasySSLSocketFactory.class));
            }

            public int hashCode() {
                return EasySSLSocketFactory.class.hashCode();
            }
            }


        public class EasyX509TrustManager implements X509TrustManager {

            private X509TrustManager standardTrustManager = null;

            /**
             * Constructor for EasyX509TrustManager.
             */
            public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
                super();
                TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                factory.init(keystore);
                TrustManager[] trustmanagers = factory.getTrustManagers();
                if (trustmanagers.length == 0) {
                    throw new NoSuchAlgorithmException("no trust manager found");
                }
                this.standardTrustManager = (X509TrustManager) trustmanagers[0];
            }

            /**
             * @see javax.net.ssl.X509TrustManager#checkClientTrusted(X509Certificate[],String authType)
             */
            public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
                standardTrustManager.checkClientTrusted(certificates, authType);
            }

            /**
             * @see javax.net.ssl.X509TrustManager#checkServerTrusted(X509Certificate[],String authType)
             */
            public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
                if ((certificates != null) && (certificates.length == 1)) {
                    certificates[0].checkValidity();
                } else {
                    standardTrustManager.checkServerTrusted(certificates, authType);
                }
            }

            /**
             * @see javax.net.ssl.X509TrustManager#getAcceptedIssuers()
             */
            public X509Certificate[] getAcceptedIssuers() {
                return this.standardTrustManager.getAcceptedIssuers();
            }
            }


        public static HttpClient getNewHttpClient() {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

                return new DefaultHttpClient(ccm, params);
            } catch (Exception e) {
                return new DefaultHttpClient();
            }
        }
