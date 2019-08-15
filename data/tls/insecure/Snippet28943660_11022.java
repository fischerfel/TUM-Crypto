public class MySSLSocketFactory extends SSLSocketFactory {

        private SSLContext sc;
        private SSLSocketFactory ssf;  

        public MySSLSocketFactory() {
            try {
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, null);
                ssf = sc.getSocketFactory();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }  
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose)
                throws IOException {
            SSLSocket ss = (SSLSocket) ssf.createSocket(s, host, port, autoClose);
            ss.setEnabledProtocols(ss.getSupportedProtocols());
            ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
            return ss;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return ssf.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return ssf.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            SSLSocket ss = (SSLSocket) ssf.createSocket(host, port);
            ss.setEnabledProtocols(ss.getSupportedProtocols());
            ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
            return ss;
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            SSLSocket ss = (SSLSocket) ssf.createSocket(host, port);
            ss.setEnabledProtocols(ss.getSupportedProtocols());
            ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
            return ss;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                throws IOException, UnknownHostException {
            SSLSocket ss = (SSLSocket) ssf.createSocket(host, port, localHost, localPort);
            ss.setEnabledProtocols(ss.getSupportedProtocols());
            ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
            return ss;
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
                int localPort) throws IOException {
            SSLSocket ss = (SSLSocket) ssf.createSocket(address, port, localAddress, localPort);
            ss.setEnabledProtocols(ss.getSupportedProtocols());
            ss.setEnabledCipherSuites(ss.getSupportedCipherSuites());
            return ss;
        }
    }
