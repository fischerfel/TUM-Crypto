public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
    try {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

        Socket socket = sslSocketFactory.createSocket("127.0.0.1", 1025);

        OutputStream out = socket.getOutputStream();

        out.write("hello".getBytes());

        Thread.sleep(2000);

        out.write("hello again".getBytes());
        out.write("hello again again".getBytes());


    } catch (IOException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
