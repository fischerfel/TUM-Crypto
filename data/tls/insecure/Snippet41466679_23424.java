SSLContext context = SSLContext.getInstance("TLS");
context.init(null, null, null);
SSLSocketFactory noSSLv3Factory = null;
if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
    noSSLv3Factory = new TLSSocketFactory(sslContext.getSocketFactory());
} else {
    noSSLv3Factory = sslContext.getSocketFactory();
}
connection.setSSLSocketFactory(noSSLv3Factory);
