public class HttpsSecurityFilter implements Filter {
    private boolean _isHTTSEnabled = false;
    private long _httsMaxAge = -1;

    private boolean _isHPKPEnabled = false;
    private long _hpkpMaxAge = -1;
    private String _hpkpCertHashesString = null;

    private boolean _includeSubdomains = false;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                FilterChain chain) throws IOException, ServletException {

        if (_isHTTSEnabled && response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).setHeader("Strict-Transport-Security", 
                                                       "max-age=" + _httsMaxAge);
        }

        if (_isHPKPEnabled && response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).setHeader("Public-Key-Pins", 
                                                       _hpkpCertHashesString);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        //Read from application configuration file
        _isHTTSEnabled = true;
        _httsMaxAge = 5184000;

        _isHPKPEnabled = true;
        _hpkpMaxAge = 5184000;
        _includeSubdomains = false;

        if (_isHTTSEnabled) {

            try {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                InputStream is = new FileInputStream(APP_HOME + "/conf/keystore");
                keyStore.load(is, keysotrePassword);
                is.close();

                Certificate cert = keyStore.getCertificate(myappcetificatealias);

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] digest = md.digest(cert.getPublicKey().getEncoded());
                _hpkpCertHashesString = "pin-sha256=\"" + java.util.Base64.getEncoder()
                                        .encodeToString(digest) + "\"; max-age=" + 
                                         _hpkpMaxAge;
            } catch (Exception e) {
                ;
            }
        }
    }
}
