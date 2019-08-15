@Test
public void test() {
    final WebClient wc = new WebClient(BrowserVersion.FIREFOX_10);
    wc.getCookieManager().setCookiesEnabled(true);
    wc.setJavaScriptEnabled(false);
    wc.setWebConnection(new TestHttpWebConnection(wc));
    try {
        final HtmlPage page = wc.getPage("https://developer.paypal.com/");
        System.out.println(page.asXml());
        final List<HtmlAnchor> anchors = page.getAnchors();
        HtmlAnchor loginAnchor = null;
        for (final HtmlAnchor htmlAnchor : anchors) {
            if (htmlAnchor.getHrefAttribute().startsWith("https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize?client_id")) {
                loginAnchor = htmlAnchor;
                break;
            }
        }
        if (loginAnchor != null) {
            System.out.println("### login anchor");
            System.out.println(loginAnchor.asXml());
            System.out.println("### login to: " + loginAnchor.getHrefAttribute());
            final HtmlPage loginPage = wc.getPage(loginAnchor.getHrefAttribute());
            System.out.println("### login page");
            System.out.println(loginPage.asXml());
            final HtmlForm loginForm = loginPage.getForms().get(0);
            final HtmlInput email = loginForm.getInputByName("email");
            final HtmlInput password = loginForm.getInputByName("password");
            final HtmlInput login = loginForm.getInputByValue("Log In");
            email.setValueAttribute("my@email.com");
            password.setValueAttribute("password");
            final HtmlPage loggedInPage = login.click();
            System.out.println("### logged in page");
            System.out.println(loggedInPage.asXml());

        }
        final HtmlPage pageLoggedIn = wc.getPage("https://developer.paypal.com/");
        System.out.println("### page logged in ");
        System.out.println(pageLoggedIn.asXml());

    } catch (final FailingHttpStatusCodeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (final MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (final IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}

public static final class EasySSLSocketFactory implements SchemeLayeredSocketFactory {

    // ** Log object for this class. *//*
    private static final Log LOG = LogFactory.getLog(EasySSLSocketFactory.class);

    private SSLContext sslcontext = null;

    public Socket createSocket(final HttpParams params) throws IOException {
        final SSLSocket sock = (SSLSocket) getSSLContext().getSocketFactory().createSocket();
        return sock;
    }

    public Socket createLayeredSocket(final Socket socket, final String target, final int port, final HttpParams params) throws IOException,
        UnknownHostException {
        final SSLSocket sslSocket = (SSLSocket) getSSLContext().getSocketFactory().createSocket(socket, target, port, true);
        // verifyHostName() didn't blowup - good!
        return sslSocket;
    }

    public Socket connectSocket(final Socket socket, final InetSocketAddress remoteAddress, final InetSocketAddress localAddress, final HttpParams params)
        throws IOException, UnknownHostException, ConnectTimeoutException {

        if (remoteAddress == null)
            throw new IllegalArgumentException("Remote address may not be null");
        if (params == null)
            throw new IllegalArgumentException("HTTP parameters may not be null");
        final Socket sock = socket != null ? socket : getSSLContext().getSocketFactory().createSocket();
        if (localAddress != null) {
            sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
            sock.bind(localAddress);
        }

        final int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
        final int soTimeout = HttpConnectionParams.getSoTimeout(params);

        try {
            sock.setSoTimeout(soTimeout);
            sock.connect(remoteAddress, connTimeout);
        } catch (final SocketTimeoutException ex) {
            throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
        }

        String hostname;
        if (remoteAddress instanceof HttpInetSocketAddress) {
            hostname = ((HttpInetSocketAddress) remoteAddress).getHttpHost().getHostName();
        } else {
            hostname = remoteAddress.getHostName();
        }

        SSLSocket sslsock;
        // Setup SSL layering if necessary
        if (sock instanceof SSLSocket) {
            sslsock = (SSLSocket) sock;
        } else {
            final int port = remoteAddress.getPort();
            sslsock = (SSLSocket) getSSLContext().getSocketFactory().createSocket(sock, hostname, port, true);
        }
        return sslsock;
    }

    public boolean isSecure(final Socket sock) throws IllegalArgumentException {
        if (sock == null)
            throw new IllegalArgumentException("Socket may not be null");
        // This instanceof check is in line with createSocket() above.
        if (!(sock instanceof SSLSocket))
            throw new IllegalArgumentException("Socket not created by this factory");
        // This check is performed last since it calls the argument object.
        if (sock.isClosed())
            throw new IllegalArgumentException("Socket is closed");
        return true;
    }

    private static SSLContext createEasySSLContext() {
        try {
            final SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
            return context;
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            throw new HttpClientError(e.toString());
        }
    }

    private SSLContext getSSLContext() {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

}

public static final class TestHttpWebConnection extends HttpWebConnection {

    public TestHttpWebConnection(final WebClient webClient) {
        super(webClient);

        final SchemeRegistry schemeRegistry = getHttpClient().getConnectionManager().getSchemeRegistry();
        final SchemeSocketFactory socketFactory = new EasySSLSocketFactory();
        schemeRegistry.register(new Scheme("https", 443, socketFactory));
    }
}
