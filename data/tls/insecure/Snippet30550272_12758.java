String oauth_signature_method = "HMAC-SHA1";

    // generate any fairly random alphanumeric string as the "nonce".
    String uuid_string = UUID.randomUUID().toString();
    uuid_string = uuid_string.replaceAll("-", "");
    String oauth_nonce = uuid_string;

    // get the timestamp
    Calendar tempcal = Calendar.getInstance();
    long ts = tempcal.getTimeInMillis();
    String oauth_timestamp = (new Long(ts / 1000)).toString(); 
    String parameter_string = "oauth_callback=" + OauthConstants.TWITTER_OAUTH_CALLBACK 
            + "&oauth_consumer_key=" + OauthConstants.TWITTER_OAUTH_CONSUMER_KEY
            + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method="
            + oauth_signature_method + "&oauth_timestamp=" + oauth_timestamp + "&oauth_version=1.0";
    String signature_base_string = get_or_post + "&" + encode(twitter_endpoint) + "&" + encode(parameter_string);
    String oauth_signature = "";

    try {
        oauth_signature = computeSignature(signature_base_string, OauthConstants.TWITTER_OAUTH_CONSUMER_SECRET + "&");  
    } catch (GeneralSecurityException | UnsupportedEncodingException e) {
       ...}

String twitter_endpoint = "https://api.twitter.com/oauth/request_token";
String authorization_header_string = "OAuth oauth_callback=\"" + OauthConstants.TWITTER_OAUTH_CALLBACK
                + "\",oauth_consumer_key=\"" + OauthConstants.TWITTER_OAUTH_CONSUMER_KEY
                + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp
                + "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\""
                + encode(oauth_signature) + "\"";

// Apache httpcore 4.4.1
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
                // initialize the HTTPS connection
                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, null, null);
                SSLSocketFactory ssf = sslcontext.getSocketFactory();
                Socket socket = ssf.createSocket();
                socket.connect(new InetSocketAddress(host.getHostName(), host.getPort()), 0);
                conn.bind(socket);

                BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest("POST", twitter_endpoint_path, HttpVersion.HTTP_1_1);
                request2.setEntity(new StringEntity("", "UTF-8"));
                request2.addHeader("Authorization", authorization_header_string); 
                httpexecutor.preProcess(request2, httpproc, context);
                HttpResponse response2 = httpexecutor.execute(request2, conn, context);
                httpexecutor.postProcess(response2, httpproc, context);
} catch(Exception e) {} ... 
