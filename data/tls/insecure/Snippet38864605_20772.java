public class WebViewFragment extends Fragment {

    public final String TAG = WebViewFragment.class.getSimpleName();
    private WebView webView;

    public static final int DEFAULT_BUFFER_SIZE = 2048;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public WebViewFragment() {

    }

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_webview_fragment, container, false);

        initGlobal(view);
        return view;
    }

    private void initGlobal(View view) {
        webView = (WebView) view.findViewById(R.id.webview);
        //MyBrowser is a custom class which extends Webviewclient which loads the given url in the webview
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

//        webView.loadUrl("http://www.google.com");
//        webView.loadUrl("https://www.github.com");
//        webView.loadUrl("https://eaadhaar.uidai.gov.in/");

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(getActivity().getResources().openRawResource(R.raw.newcertificate));
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create a SSLContext with the certificate
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Create a HTTPS connection
            URL url = new URL("https://eaadhaar.uidai.gov.in");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());

            InputStream in = conn.getInputStream();
            int lastResponseCode = conn.getResponseCode();
            Log.e(TAG, "response code=" + lastResponseCode);

            if (lastResponseCode == 200) {
                Toast.makeText(getActivity(), "Response code==" + lastResponseCode, Toast.LENGTH_SHORT).show();
            }
            copyInputStreamToOutputStream(in, System.out, 2048, true, true);


        } catch (Exception e) {
            Log.e(TAG, "Exception========" + e.toString());
        }
    }

    public void copyInputStreamToOutputStream(InputStream from, OutputStream to, int bufferSize, boolean closeInput, boolean closeOutput) {
        try {
            int totalBytesRead = 0;
            int bytesRead = 0;
            int offset = 0;
            byte[] data = new byte[bufferSize];

            while ((bytesRead = from.read(data, offset, bufferSize)) > 0) {
                totalBytesRead += bytesRead;
                to.write(data, offset, bytesRead);
                Log.e(TAG, "Copied " + totalBytesRead + " bytes");
            }
            closeStreams(from, to, closeInput, closeOutput);
        } catch (Exception e) {
            closeStreams(from, to, closeInput, closeOutput);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void closeStreams(InputStream from, OutputStream to, boolean closeInput, boolean closeOutput) {
        try {
            if (to != null)
                to.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (closeInput && from != null)
                from.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (closeOutput && to != null)
                to.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
