public class Final extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);


        Button btnSend = (Button)findViewById(R.id.BtnSend);



        btnSend.setOnClickListener(new OnClickListener(){

        public void onClick(View arg0){

                TestReceiver();


            }
        });



    void TestReceiver()
    {

            //ALERT MESSAGE
            Toast.makeText(getBaseContext(),"Please wait, connecting to server.",Toast.LENGTH_LONG).show();

            HttpClient Client = new DefaultHttpClient();

            try
            {

                Client.getConnectionManager().getSchemeRegistry().register(getMockedScheme());

            } catch (Exception e) {
                System.out.println("Error con no se que");
            }


            String Value="Kevin";

            String URL = "http://echogame24.comli.com/Receiver.php?action="+Value;


            try
            {
                          String SetServerString = "";

                        // Create Request to server and get response

                         HttpGet httpget = new HttpGet(URL);
                         ResponseHandler<String> responseHandler = new BasicResponseHandler();
                         SetServerString = Client.execute(httpget, responseHandler);

             }
           catch(Exception ex)
           {
                    System.out.println("Fail!");
           }

    }



   //** I added all this following the advise in the link below


    public Scheme getMockedScheme() throws Exception {
        MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory();
        return new Scheme("https", (SocketFactory) mySSLSocketFactory, 443);
    }

    class MySSLSocketFactory extends SSLSocketFactory {
        javax.net.ssl.SSLSocketFactory socketFactory = null;

        public MySSLSocketFactory(KeyStore truststore) throws Exception {
            super(truststore);
            socketFactory = getSSLSocketFactory();
        }

        public MySSLSocketFactory() throws Exception {
            this(null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException {
            return socketFactory.createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return socketFactory.createSocket();
        }

        javax.net.ssl.SSLSocketFactory getSSLSocketFactory() throws Exception {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslContext.init(null, new TrustManager[] { tm }, null);
            return sslContext.getSocketFactory();
        }
    }


}
