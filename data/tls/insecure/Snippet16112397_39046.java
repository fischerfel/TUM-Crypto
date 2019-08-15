    .  .. . . . . .. . . . .
    System.out.println("--Enter HttpClient.getSSLHttpClient--");

    // a trust manager that trusts everything
    X509TrustManager allTrustManager = new AllTrustManager();

    // SSLContext
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] { allTrustManager }, null);

    SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext);
    Scheme httpsScheme = new Scheme("https", port,sslSocketFactory);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);

    ClientConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);

    //System.out.println("--Exit HttpClient.getSSLHttpClient--");

    DefaultHttpClient httpClient = new DefaultHttpClient(cm,getHttpParams());

    System.out.println("--Exit HttpClient.getSSLHttpClient--");

    return httpClient;  
    .  .. . . . . .. . . . .



Here is the stake trace:

11:58:27.593 [main] DEBUG o.a.h.i.c.BasicClientConnectionManager - Get connection for route {s}->https://xyz.com:443
11:58:27.786 [main] DEBUG o.a.h.i.c.DefaultClientConnectionOperator - Connecting to xyz.com:443
11:58:29.379 [main] DEBUG o.a.h.c.protocol.RequestAddCookies - CookieSpec selected: best-match
11:58:29.387 [main] DEBUG o.a.h.c.protocol.RequestAuthCache - Auth cache not set in the context
11:58:29.387 [main] DEBUG o.a.h.c.p.RequestTargetAuthentication - Target auth state: UNCHALLENGED
11:58:29.388 [main] DEBUG o.a.h.c.p.RequestProxyAuthentication - Proxy auth state: UNCHALLENGED
11:58:29.388 [main] DEBUG o.a.h.impl.client.DefaultHttpClient - Attempt 1 to execute request
11:58:29.388 [main] DEBUG o.a.h.i.conn.DefaultClientConnection - Sending request: POST /user?username=abc&password=123 HTTP/1.1
11:58:29.388 [main] DEBUG org.apache.http.wire - >> "POST /user?username=abc&password=123 HTTP/1.1[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "Keep-Alive: true[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "Accept: text/html[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "Content-Length: 0[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "Host: xyz.com:443[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "Connection: Keep-Alive[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.wire - >> "[\r][\n]"
11:58:29.389 [main] DEBUG org.apache.http.headers - >> POST /user?username=abc&password=123 HTTP/1.1
11:58:29.389 [main] DEBUG org.apache.http.headers - >> Keep-Alive: true
11:58:29.389 [main] DEBUG org.apache.http.headers - >> Accept: text/html
11:58:29.389 [main] DEBUG org.apache.http.headers - >> Content-Length: 0
11:58:29.390 [main] DEBUG org.apache.http.headers - >> Host: xyz.com:443
11:58:29.390 [main] DEBUG org.apache.http.headers - >> Connection: Keep-Alive
11:58:29.660 [main] DEBUG o.a.h.i.conn.DefaultClientConnection - Connection 0.0.0.0:53007<->xx.xx.xx.xx:443 closed
11:58:29.660 [main] DEBUG o.a.h.impl.client.DefaultHttpClient - Closing the connection.
11:58:29.660 [main] DEBUG o.a.h.i.conn.DefaultClientConnection - Connection 0.0.0.0:53007<->xx.xx.xx.xx:443 closed
11:58:29.660 [main] DEBUG o.a.h.i.conn.DefaultClientConnection - Connection 0.0.0.0:53007<->xx.xx.xx.xx:443 shut down
11:58:29.661 [main] DEBUG o.a.h.i.c.BasicClientConnectionManager - Releasing connection org.apache.http.impl.conn.ManagedClientConnectionImpl@4f8bc5eb
11:58:29.661 [main] DEBUG o.a.h.i.c.BasicClientConnectionManager - Connection can be kept alive for 9223372036854775807 MILLISECONDS
java.net.SocketException: Connection reset
    at java.net.SocketInputStream.read(SocketInputStream.java:189)
    at java.net.SocketInputStream.read(SocketInputStream.java:121)
    at sun.security.ssl.InputRecord.readFully(InputRecord.java:312)
    at sun.security.ssl.InputRecord.read(InputRecord.java:350)
    at sun.security.ssl.SSLSocketImpl.readRecord(SSLSocketImpl.java:927)
    at sun.security.ssl.SSLSocketImpl.readDataRecord(SSLSocketImpl.java:884)
    at sun.security.ssl.AppInputStream.read(AppInputStream.java:102)
    at org.apache.http.impl.io.AbstractSessionInputBuffer.fillBuffer(AbstractSessionInputBuffer.java:166)
    at org.apache.http.impl.io.SocketInputBuffer.fillBuffer(SocketInputBuffer.java:90)
    at org.apache.http.impl.io.AbstractSessionInputBuffer.readLine(AbstractSessionInputBuffer.java:281)
    at org.apache.http.impl.conn.LoggingSessionInputBuffer.readLine(LoggingSessionInputBuffer.java:115)
    at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:92)
    at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:62)
    at org.apache.http.impl.io.AbstractMessageParser.parse(AbstractMessageParser.java:254)
    at org.apache.http.impl.AbstractHttpClientConnection.receiveResponseHeader(AbstractHttpClientConnection.java:289)
    at org.apache.http.impl.conn.DefaultClientConnection.receiveResponseHeader(DefaultClientConnection.java:252)
    at org.apache.http.impl.conn.ManagedClientConnectionImpl.receiveResponseHeader(ManagedClientConnectionImpl.java:191)
    at org.apache.http.protocol.HttpRequestExecutor.doReceiveResponse(HttpRequestExecutor.java:300)
    at org.apache.http.protocol.HttpRequestExecutor.execute(HttpRequestExecutor.java:127)
    at org.apache.http.impl.client.DefaultRequestDirector.tryExecute(DefaultRequestDirector.java:717)
    at org.apache.http.impl.client.DefaultRequestDirector.execute(DefaultRequestDirector.java:522)
    at org.apache.http.impl.client.AbstractHttpClient.execute(AbstractHttpClient.java:906)
    at org.apache.http.impl.client.AbstractHttpClient.execute(AbstractHttpClient.java:805)
    at org.apache.http.impl.client.AbstractHttpClient.execute(AbstractHttpClient.java:784)
