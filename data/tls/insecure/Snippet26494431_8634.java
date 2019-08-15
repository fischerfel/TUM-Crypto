OkHttpClient okHttpClient = new OkHttpClient();
try {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
    okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
} catch (GeneralSecurityException ignored) {
}

// open a URL connection to the Servlet
fileInputStream = mImagePath.getContentStream();
URL url = new URL(Api.url(Api.Methods.UPLOAD_IMAGE));

conn = okHttpClient.open(url); // FIX!!!
conn.setDoInput(true);
conn.setDoOutput(true);
conn.setUseCaches(false);
conn.setRequestMethod("POST");
