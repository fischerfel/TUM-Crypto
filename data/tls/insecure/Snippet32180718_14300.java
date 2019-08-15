public class AppTest
{
    public static void main(String arg[]) throws IOException
    {
        ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        AppTest.trustSelfSignedSSL();
         RestTemplate restTemplate = new RestTemplate(messageConverters);


           System.out.println("ok");


            ResponseEntity<byte[]> response = restTemplate.exchange(
                    "https://jira.example.com/rest/api/2/issue/id",
                    HttpMethod.GET, new HttpEntity<String>(createHeaders()), byte[].class, "1");
            System.out.println(response.getBody());
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("inside");
                Files.write(Paths.get("file.html"), response.getBody());
            }


    }

    static HttpHeaders createHeaders(){
        String plainCreds = "user:pass";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    public static void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLContext.setDefault(ctx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



}
}
