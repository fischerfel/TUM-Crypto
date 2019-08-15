SSLContext sslContext;
try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(
            null,
            new TrustManager[]{...<custom trust manager stuff>...},
            null
    );
} catch (NoSuchAlgorithmException nsae) {
    FileLog.e(TAG, "No such algorithm: TLS: " + nsae.getMessage());
    return null;
} catch (KeyManagementException kme) {
    FileLog.e(TAG, "Key management problem: " + kme.getMessage());
    return null;
}
FTPSClient client = new FTPSClient(sslContext);
client.setControlEncoding("UTF-8");
client.connect(ip, port); // <---- needs a long time
