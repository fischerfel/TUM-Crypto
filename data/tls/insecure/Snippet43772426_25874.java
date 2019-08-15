public static SSLContext createDefault() throws SSLInitializationException {
    try {
        SSLContext ex = SSLContext.getInstance("TLS");
        ex.init((KeyManager[])null, (TrustManager[])null, (SecureRandom)null);
        return ex;
    } catch (NoSuchAlgorithmException var1) {
        throw new SSLInitializationException(var1.getMessage(), var1);
    } catch (KeyManagementException var2) {
        throw new SSLInitializationException(var2.getMessage(), var2);
    }
}
