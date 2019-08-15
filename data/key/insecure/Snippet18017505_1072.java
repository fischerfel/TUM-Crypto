@SuppressWarnings("serial")
public class GoogleServlet {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String PROJECT_ID = "";

    public static String Base64Encoding()
            throws java.security.SignatureException, UnsupportedEncodingException {

          String access_id = "GOOG37E2YNNQW6FIGGDS ";
          String secret_key = URLEncoder.encode("","UTF-8");
          String bucket = "";

          String version_header = "x-goog-api-version:1";
          String project_header = "x-goog-project-id:"+PROJECT_ID;
          String canonicalizedResources = "/"+bucket+"/";

          Calendar calendar = Calendar.getInstance();
          calendar.add(Calendar.MINUTE, 30);
          long expiration = calendar.getTimeInMillis();

         String stringToSign = URLEncoder.encode("GET\n\n\n"+expiration+"\n"+version_header+"\n"+project_header+"\n"+canonicalizedResources,"UTF-8");
         //String stringToSign = URLEncoder.encode("GET\n\n\n"+getdate()+"\n"+version_header+"\n"+project_header+"\n"+canonicalizedResources,"UTF-8");
         String authSignature="";
        try {

            SecretKeySpec signingKey = new SecretKeySpec(secret_key.getBytes(),HMAC_SHA1_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(stringToSign.getBytes("UTF-8"));

            // base64-encode the hmac
            authSignature = new String(Base64.encodeBase64(rawHmac));

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        authSignature = (access_id +":"+ authSignature);
        return  authSignature;
    }

    public static void main(String[] args) {

        ClientConfig config = new DefaultClientConfig();-->ClientConfig cannot be resolved to a type
        Client client = Client.create(config);

        String authSignature = null;
        try {
            authSignature = "GOOG1 "+ Base64Encoding();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        WebResource service = client.resource(getBaseURI());
        ClientResponse response = service.accept(MediaType.APPLICATION_XML)-->ClientResponse cannot be resolved to a type
                .header("Authorization",authSignature)
                .header("Date", getdate())
                .header("Content-Length", "0")
                .header("x-goog-api-version", "1")
                .header("x-goog-project-id", PROJECT_ID)
                .get(ClientResponse.class);

        System.out.println(response.getClientResponseStatus().getFamily());
        System.out.println("response1 :: " + response.getEntity(String.class));

    }

    private static URI getBaseURI() {
        String url = "https://storage.cloud.google.com/mss/";
        return UriBuilder.fromUri(url).build();--->The method resource(URI) is undefined for the type Client
    }
    private static String getdate(){
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z ", new Locale("US"));
         Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));

         format.setCalendar(cal);
         return format.format(new Date(0));
    }
}
