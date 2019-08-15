            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLCertificateSocketFactory ssf = (SSLCertificateSocketFactory) SSLCertificateSocketFactory
                    .getDefault(0);

            ssf.setTrustManagers(new TrustManager[] { tm });

            m_sslsocket = (SSLSocket) ssf.createSocket(m_socket, m_host, m_port, false);
