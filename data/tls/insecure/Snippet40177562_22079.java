TrustManager[] trustArray = [new TrustAll()] as TrustManager[]
SSLContext ctx = SSLContext.getInstance("TLSv1");
ctx.init(null, trustArray, null);

// create an http client builder
HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
 httpClientBuilder.setSslcontext(ctx)

 httpclient = httpClientBuilder.build();
