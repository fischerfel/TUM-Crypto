final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
   @Override
   public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
   }

   @Override
   public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
   }

   @Override
   public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
   }
 }
};

// Install the all-trusting trust manager
final SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
OkHttpClient client = new OkHttpClient();
client.setSslSocketFactory(sslContext.getSocketFactory());
client.setHostnameVerifier(new HostnameVerifier() {
       @Override
       public boolean verify(String hostname, SSLSession session) {
              return true;
       }
});
client.networkInterceptors().add(new StethoInterceptor());

File pngFile = new File(Environment.getExternalStorageDirectory() + "signature_" + pdfFile.getFileName() + ".png");
File pdf = new File(Environment.getExternalStorageDirectory() + pdfFile.getFileName());

String mode = "SampleMode"

Headers headers = new Headers.Builder()
        .add("Content-Type", "multipart/form-data")
        .build();

MultipartBuilder multipartBuilder = new MultipartBuilder()
       .type(MultipartBuilder.FORM)
       .addFormDataPart("Req", "SampleRequest")
       .addFormDataPart("Token", "MyAPIToken")
       .addFormDataPart("Mode", mode)
       .addFormDataPart("SrcFile", pdf.getAbsolutePath(), RequestBody.create(MediaType.parse("application/pdf"), pdf))
       .addFormDataPart("SignFile", pngFile.getAbsolutePath(), RequestBody.create(MediaType.parse("image/png"), pngFile));

RequestBody requestBody = multipartBuilder
             .build();

Request request = new Request.Builder()
             .url("MyURLHere")
             .headers(headers)
             .post(requestBody)
             .build();

Response response = client.newCall(request).execute();
