public class XMLParsingExample extends Activity {

    /** Create Object For SiteList Class */
    SitesList sitesList = null;
     URL url;
     HttpsURLConnection https;
     HttpURLConnection conn = null;
     final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {

            return true;
        }
  };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



            try {
                url = new URL("https://222.165.187.91/ex_rate/XML_LOLC_EXRT.xml");
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();

                try {
                    https = (HttpsURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        /** Create a new layout to display the view */
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);

        /** Create a new textview array to display the results */
        TextView From_Currency[];
        TextView To_Currency[];
        TextView exrt_buy[];
        TextView exrt_sell[];

        try {

            /** Handling XML */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            /** Send URL to parse XML Tags */
            URL sourceUrl = new URL("https://222.165.187.91/ex_rate/XML_LOLC_EXRT.xml");

            /** Create handler to handle XML Tags ( extends DefaultHandler ) */
            MyXMLHandler myXMLHandler = new MyXMLHandler();
            xr.setContentHandler(myXMLHandler);
            xr.parse(new InputSource(sourceUrl.openStream()));

        } catch (Exception e) {
            System.out.println("XML Pasing Excpetion = " + e);
        }

        /** Get result from MyXMLHandler SitlesList Object */
        sitesList = MyXMLHandler.sitesList;

        /** Assign textview array lenght by arraylist size */
        From_Currency = new TextView[sitesList.getFrom_Currency().size()];
        To_Currency = new TextView[sitesList.getTo_Currency().size()];
        exrt_buy = new TextView[sitesList.getexrt_buy().size()];
        exrt_sell = new TextView[sitesList.getexrt_sell().size()];

        /** Set the result text in textview and add it to layout */
        for (int i = 0; i < sitesList.getFrom_Currency().size(); i++) {
            From_Currency[i] = new TextView(this);
            From_Currency[i].setText("Name = "+sitesList.getFrom_Currency().get(i));

            To_Currency[i] = new TextView(this);
            To_Currency[i].setText("Website = "+sitesList.getTo_Currency().get(i));

            exrt_buy[i] = new TextView(this);
            exrt_buy[i].setText("Website Category = "+sitesList.getexrt_buy().get(i));

            exrt_sell[i] = new TextView(this);
            exrt_sell[i].setText("Website Category = "+sitesList.getexrt_sell().get(i));

            layout.addView(From_Currency[i]);
            layout.addView(To_Currency[i]);
            layout.addView(exrt_buy[i]);
            layout.addView(exrt_sell[i]);
        }

        /** Set the layout view to display */
        setContentView(layout);

    }

    private void trustAllHosts() {
          // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[] {};
                }


                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                    // TODO Auto-generated method stub

                }
        } };

        // Install the all-trusting trust manager
        try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection
                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
                e.printStackTrace();
        }

    }
}
