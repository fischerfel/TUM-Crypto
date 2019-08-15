public class TrustedSSLSocketFactory extends SSLSocketFactory {
    private static SSLContext context;
    public static void initTrustedSSLSocketFactory(final KeyStore keyStore) throws Exception {
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);

        final SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
    }

    public static SocketFactory getDefault() {
        return context.getSocketFactory();
    }

    public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
        return trustedFactory.createSocket(arg0, arg1);
    }

    public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
        return trustedFactory.createSocket(arg0, arg1);
    }

    public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException {
        return trustedFactory.createSocket(arg0, arg1, arg2, arg3);
    }

    public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
        return trustedFactory.createSocket(arg0, arg1, arg2, arg3);
    }
}
