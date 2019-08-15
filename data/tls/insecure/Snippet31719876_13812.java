Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
TrustManager[] trustCerts = new TrustManager[]{new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                       public void checkServerTrusted(X509Certificate[]
                       certs, String authType) throws CertificateException {
                            return;
                        }

                        public void checkClientTrusted(X509Certificate[]
                        certs, String authType) throws CertificateException {
                            return;
                        }
                    }
            }; 
SSLContext sc = SSLContext.getInstance("SSLv3");
sc.init(null, trustCerts, null);
SocketFactory factory = sc.getSocketFactory();
SSLSocket socket;
socket = (SSLSocket) factory.createSocket(mInstance.getHostName(),getSecureConnectionEndpoint().getPort());
socket.startHandshake();
setCerts(socket.getSession().getPeerCertificates());
