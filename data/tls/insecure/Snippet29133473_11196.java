 protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sslclient);

    //Allowing to access the network
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

    //Getting the GUI user input information
    web_address_et              = (EditText)findViewById(R.id.editText1);
    port_number_et              = (EditText)findViewById(R.id.editText2);
    connection_information_tv   = (TextView)findViewById(R.id.textView3);

    findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findViewById(R.id.button1).setBackgroundColor(Color.CYAN);
            findViewById(R.id.button2).setBackgroundColor(color.button_material_dark);
            if (web_address_et.getText().toString().matches("") 
                || port_number_et.getText().toString().matches("")) {
                connection_information_tv.setText("Please fill the URL and Port# fields!");
            }

            else {
                try {
                    InetAddress host = InetAddress.getByName(web_address_et.getText().toString());

                    //Android native behavior (do not accept untrusted certificate)
                    SSLSocketFactory socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
                    SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, Integer.parseInt(port_number_et.getText().toString()));

                    socket.startHandshake();
                    printSSLSessionInfo(socket, socket.getSession());
                    socket.close();

                    } catch (UnknownHostException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (SSLPeerUnverifiedException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (IOException e) {
                        connection_information_tv.setText(e.toString());
                    }
            }
        }
    });

    findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            findViewById(R.id.button1).setBackgroundColor(color.button_material_dark);
            findViewById(R.id.button2).setBackgroundColor(Color.CYAN);
            if (web_address_et.getText().toString().matches("") 
                || port_number_et.getText().toString().matches("")) {
                connection_information_tv.setText("Please fill the URL and Port# fields!");
            }

            else {
                try {
                    InetAddress host = InetAddress.getByName(web_address_et.getText().toString());

                    //Naive custom TrustManager (empty checkServerTrusted)
                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    TrustManager trustManagerNaive =    new X509TrustManager(){
                                                            @Override
                                                            public void checkClientTrusted(
                                                                    X509Certificate[] chain,
                                                                    String authType)
                                                                    throws CertificateException {
                                                                // TODO Auto-generated method stub
                                                            }

                                                            @Override
                                                            public X509Certificate[] getAcceptedIssuers() {
                                                                // TODO Auto-generated method stub
                                                                return null;
                                                            }

                                                            @Override
                                                            public void checkServerTrusted(
                                                                    X509Certificate[] chain,
                                                                    String authType)
                                                                    throws CertificateException {
                                                                // TODO Auto-generated method stub
                                                            }
                                                        };

                    sslContext.init(null, new TrustManager[]{trustManagerNaive}, new SecureRandom());

                    SSLSocketFactory socketFactory = (SSLSocketFactory)sslContext.getSocketFactory();
                    SSLSocket socket = (SSLSocket)socketFactory.createSocket(host, Integer.parseInt(port_number_et.getText().toString()));

                    socket.startHandshake();
                    printSSLSessionInfo(socket, socket.getSession());
                    socket.close();

                    } catch (UnknownHostException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (NoSuchAlgorithmException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (KeyManagementException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (SSLPeerUnverifiedException e) {
                        connection_information_tv.setText(e.toString());
                    } catch (IOException e) {
                        connection_information_tv.setText(e.toString());
                    }
            }
        }
    });
}
