    public JSONObject getTwitterAccessTokenFromAuthorizationCode(String verifier_or_pin, String oauth_token) {
    ...
 String oauth_signature_method = "HMAC-SHA1";

    // generate any fairly random alphanumeric string as the "nonce". Nonce = Number used ONCE.
    String uuid_string = UUID.randomUUID().toString();
    uuid_string = uuid_string.replaceAll("-", "");
    String oauth_nonce = uuid_string; 

    Calendar tempcal = Calendar.getInstance();
    long ts = tempcal.getTimeInMillis();
    String oauth_timestamp = (new Long(ts / 1000)).toString(); 

    // the parameter string must be in alphabetical order
    String parameter_string = "oauth_consumer_key=" + OauthConstants.TWITTER_OAUTH_CONSUMER_KEY 
                            + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method
                            + "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauth_token) + "&oauth_version=1.0";

    String signature_base_string = get_or_post + "&" + encode(twitter_endpoint) + "&" + encode(parameter_string);

    String oauth_signature = "";
    try {
        oauth_signature = computeSignature(signature_base_string, OauthConstants.TWITTER_OAUTH_CONSUMER_SECRET + "&");
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
        ...
    }

    String authorization_header_string = "OAuth oauth_consumer_key=\"" + OauthConstants.TWITTER_OAUTH_CONSUMER_KEY  
                                               + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp
                                               + "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" 
                                               + encode(oauth_signature) + "\",oauth_token=\"" + encode(oauth_token) + "\"";

    HttpProcessor httpproc = HttpProcessorBuilder.create()
                    .add(new RequestContent())
                    .add(new RequestTargetHost())
                    .add(new RequestConnControl())
                    .add(new RequestUserAgent("ApacheHttp/1.1"))
                    .add(new RequestExpectContinue(true)).build();

            HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
            HttpCoreContext context = HttpCoreContext.create();
            HttpHost host = new HttpHost(twitter_endpoint_host, 443); 
            DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);

            context.setAttribute(HttpCoreContext.HTTP_CONNECTION, conn);
            context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, host);

    try {
                    SSLContext sslcontext = SSLContext.getInstance("TLS");
                    sslcontext.init(null, null, null);
                    SSLSocketFactory ssf = sslcontext.getSocketFactory();
                    Socket socket = ssf.createSocket();
                    socket.connect(new InetSocketAddress(host.getHostName(), host.getPort()), 0);
                    conn.bind(socket);

                    BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest("POST", twitter_endpoint_path);
                    // Including oauth_verifier value to request body
                    request2.setEntity(new StringEntity("oauth_verifier=" + encode(verifier_or_pin), "UTF-8"));
                    request2.addHeader("Authorization", authorization_header_string);
                    httpexecutor.preProcess(request2, httpproc, context);
                    HttpResponse response2 = httpexecutor.execute(request2, conn, context);
... 
    }
