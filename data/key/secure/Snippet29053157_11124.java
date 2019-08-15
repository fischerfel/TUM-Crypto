public class AmazonExample {
private static final String CHARACTER_ENCODING = "UTF-8";
final static String ALGORITHM = "HmacSHA256";

public static void main(String[] args) throws Exception {
    String secretKey = "/ioTb2imZWZ/IHTKKfc62BFvBxxxxxxxxxxxxxxx";
    // Use the endpoint for your marketplace
    String serviceUrl = "https://mws.amazonservices.ca/";
    // Create set of parameters needed and store in a map
    HashMap<String, String> parameters = new HashMap<String,String>();
    // Add required parameters. Change these as needed.

    parameters.put("AWSAccessKeyId", urlEncode("AKIAJUTBJCxxxxxxxxxx"));

    parameters.put("Action", urlEncode("GetServiceStatus"));    //GetOrder
    parameters.put("AmazonOrderId.Id.1", urlEncode("xxx-5684184-6801000")); 
    parameters.put("MWSAuthToken", urlEncode("amzn.mws.xxxxx7b8-5c81-3abc-06c2-c09e7dfd6ef3"));
    parameters.put("SellerId", urlEncode("xxxxYI70TZB97A"));
    parameters.put("SignatureMethod", urlEncode(ALGORITHM));
    parameters.put("SignatureVersion", urlEncode("2"));
    parameters.put("SubmittedFromDate",urlEncode("2015-03-14T17:02:05.264Z"));
    parameters.put("Timestamp", urlEncode("2015-03-14T17:02:05.264Z"));
    parameters.put("Version", urlEncode("2013-09-01"));     

    String formattedParameters = calculateStringToSignV2(parameters, serviceUrl);

    String signature = sign(formattedParameters, secretKey);

    // Add signature to the parameters and display final results
    parameters.put("Signature", urlEncode(signature));

    System.out.println(calculateStringToSignV2(parameters, serviceUrl));

    try {
        URL url = new URL("https://mws.amazonservices.ca/Orders/2013-09-01/?AWSAccessKeyId=xxxxxxxBJCIA4YSSWYNA&Action=GetServiceStatus&AmazonOrderId.Id.1=xxx-5684184-6801000&MWSAuthToken=amzn.mws.xxxxxxxx-5c81-3abc-06c2-c09e7dfd6ef3&SellerId=xxxxYI70TZB97A&Signature=yyO%2BrwMAtCcuEsYhG4KZILz2cyiSUcrAAWKqf3%2BZ454%3D&SignatureMethod=HmacSHA256&SignatureVersion=2&SubmittedFromDate=2015-03-14T17%3A02%3A05.264Z&Timestamp=2015-03-14T17%3A02%3A05.264Z&Version=2013-09-01");

            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setConnectTimeout(50000);

            BufferedReader br = null;
            StringBuffer sbOrderResponse= new StringBuffer();    
            String order = ""; String str = "";

                InputStream inputstream = null;

                 if(conn.getResponseCode() != 200 ) {                
                     inputstream = conn.getErrorStream();
                 } else{
                      inputstream = conn.getInputStream();
                 }

                 br = new BufferedReader(new InputStreamReader((inputstream))); 
                StringBuffer buffer = new StringBuffer();

                while ((order = br.readLine()) != null) {
                    sbOrderResponse.append(order);          
                    str = str + order + "\n";
                }
                System.out.println(conn.getResponseCode()  + "    " + conn.getResponseMessage());
                System.out.println(str);

            } catch(Exception e)
            {
                System.out.println("Error   " + e);
            }       
    }    
private static String calculateStringToSignV2(Map<String, String> parameters, String serviceUrl)
throws SignatureException, URISyntaxException {
    // Sort the parameters alphabetically by storing
    // in TreeMap structure
    Map<String, String> sorted = new TreeMap<String, String>(); 
    sorted.putAll(parameters);

    // Set endpoint value
    URI endpoint = new URI(serviceUrl.toLowerCase());
    // Create flattened (String) representation
    StringBuilder data = new StringBuilder();
    data.append("GET\n");
    data.append(endpoint.getHost());
    data.append("\n/");     //  /Orders/2013-09-01
    data.append("\n");
    Iterator<Entry<String, String>> pairs = sorted.entrySet().iterator();

    while (pairs.hasNext()) {
        Map.Entry<String, String> pair = pairs.next();
        if (pair.getValue() != null) {
            data.append( pair.getKey() + "=" + pair.getValue());
        }
        else {
            data.append( pair.getKey() + "=");
        }
        // Delimit parameters with ampersand (&)
        if (pairs.hasNext()) {
            data.append( "&");
        }
    }
    return data.toString();
    }
/*
* Sign the text with the given secret key and convert to base64
*/
private static String sign(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException,
IllegalStateException, UnsupportedEncodingException {

    Mac mac = Mac.getInstance(ALGORITHM);
    mac.init(new SecretKeySpec(secretKey.getBytes(CHARACTER_ENCODING), ALGORITHM));
    byte[] signature = mac.doFinal(data.getBytes(CHARACTER_ENCODING));
    String signatureBase64 = new String(Base64.encodeBase64(signature), CHARACTER_ENCODING);

    return new String(signatureBase64);
}

private static String urlEncode(String rawValue) {
    String value = (rawValue == null) ? "" : rawValue;
    String encoded = null;
    try {
        encoded = URLEncoder.encode(value, CHARACTER_ENCODING)
        .replace("+", "%20")
        .replace("*", "%2A")
        .replace("%7E","~");
        } catch (UnsupportedEncodingException e) {
    System.err.println("Unknown encoding: " + CHARACTER_ENCODING);
    e.printStackTrace();
}
return encoded;
}}
