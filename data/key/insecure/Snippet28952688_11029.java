@Override
public void process(Exchange exchange) throws Exception {


    int millis = (int) System.currentTimeMillis() * -1;
    int time = (int) millis / 1000;

    /**
     * Listing of all parameters necessary to retrieve a token
     * (sorted lexicographically as demanded)
     */
    String[][] data = {
            {"oauth_consumer_key", "<consumer_key>"},
            {"oauth_nonce",  String.valueOf(millis)},
            {"oauth_signature", ""},
            {"oauth_signature_method", "HMAC-SHA1"},
            {"oauth_timestamp", String.valueOf(time)},
            {"oauth_token", "<token>"},
            {"oauth_version", "1.0"}
    };

    HttpMethods methodObject = exchange.getIn().getHeader(Exchange.HTTP_METHOD, HttpMethods.class);
    String method = methodObject.toString();
    String uri = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
    String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
    String query = exchange.getIn().getHeader(Exchange.HTTP_QUERY, String.class);

    String url = uri;
    if (path!=null){
        url += path;
    }


    LOGGER.info("URL= "+url);

    /**
     * Generation of the signature base string
     */
    String signature_base_string =
            method+"&"+ URLEncoder.encode(url, "UTF-8")+"&";
    signature_base_string+=URLEncoder.encode(query, "UTF-8") + "%26";

    LOGGER.info("signature_base_string= "+signature_base_string);


    for(int i = 0; i < data.length; i++) {
        // ignore the empty oauth_signature field
        String additional ="";
        if(i != 2) {
            additional = URLEncoder.encode(data[i][0], "UTF-8") + "%3D" +
                    URLEncoder.encode(data[i][1], "UTF-8") ;

            if (i!= data.length-1){
                additional += "%26";
            }

            signature_base_string += additional;
            LOGGER.info("signature_base_string-Additional= "+additional);
        }

    }

    /**
     * Sign the request
     */
    Mac m = Mac.getInstance("HmacSHA1");
    m.init(new SecretKeySpec(("<consumer_secret>&<token_secrer>").getBytes(), "HmacSHA1"));
    m.update(signature_base_string.getBytes());
    byte[] res = m.doFinal();
    String sig = String.valueOf(new BASE64Encoder().encode(res));
    data[2][1] = sig;

    /**
     * Create the header for the request
     */
    String header = "OAuth ";
    for(String[] item : data) {
        header += item[0]+"=\""+item[1]+"\", ";
    }
    // cut off last appended comma
    header = header.substring(0, header.length()-2);

    String charset = "UTF-8";
    exchange.getIn().setHeader("Authorization", header);
    exchange.getIn().setHeader("Accept-Charset", charset);

}
